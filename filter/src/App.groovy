import co.freeside.hipsteroid.Filter
import org.cloudfoundry.runtime.env.*
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.http.*
import org.vertx.mods.formupload.*
import org.vertx.java.core.Handler

def cloudEnv = new CloudEnvironment()

def eventBus = vertx.eventBus

def webServerConf = [

		port: (cloudEnv.getValue('VCAP_APP_PORT') ?: '8080') as int,
		host: cloudEnv.getValue('VCAP_APP_HOST') ?: 'localhost',

		bridge: true,

		inbound_permitted: [
				[
						address_re: /hipsteroid\.filter\.\w+\.\w+/
				],
		],

		outbound_permitted: [
				[:]
		]
]

def rabbitService = cloudEnv.getServiceInfo('hipsteroid-rabbit', RabbitServiceInfo)
if (rabbitService) {
	println """Rabbit service...
name: $rabbitService.serviceName
plan: $rabbitService.plan
label: $rabbitService.label
host: $rabbitService.host
port: $rabbitService.port
userName: $rabbitService.userName
vhost: $rabbitService.virtualHost"""
} else {
	println 'No Rabbit MQ available'
}

def amqpConf = [
		uri: rabbitService ? "amqp://$rabbitService.host:$rabbitService.port" : 'amqp://localhost',
		address: 'amqp_bridge',
		defaultContentType: 'application/json'
]

def server = vertx.createHttpServer()

def bridgeConf = [prefix: '/eventbus']
vertx.createSockJSServer(server).bridge(bridgeConf, webServerConf.inbound_permitted, webServerConf.outbound_permitted)

def routeMatcher = new RouteMatcher()
routeMatcher.post('/filter/:filter') { HttpServerRequest request ->

	println 'handling request'
	def mpReq = new MultipartRequest(vertx.toJavaVertx(), request.toJavaRequest());
	mpReq.uploadHandler(new Handler<Upload>() {
		@Override
		void handle(Upload upload) {
			println 'handling upload...'
			upload.bodyHandler(new Handler<org.vertx.java.core.buffer.Buffer>() {
				@Override
				void handle(org.vertx.java.core.buffer.Buffer body) {
					println "I received ${body.length()} bytes"
					eventBus.send("hipsteroid.filter.${request.params.filter}.full", body) { Message reply ->
						println "I got filtered image back"

						def replyBuffer = new Buffer(reply.body)

						request.response.statusCode = 200
						request.response.headers['Content-Type'] = 'image/jpeg'
						request.response.headers['Content-Length'] = replyBuffer.length()
						request.response.end(replyBuffer)
					}
				}
			})
		}
	})

}

server.requestHandler(routeMatcher.asClosure()).listen(webServerConf.port)

container.with {

//	deployModule('vertx.web-server-v1.0', webServerConf)

	deployModule 'amqp-busmod#1.2.0-SNAPSHOT', amqpConf, 1, {

		println "Rabbit bridge is running... subscribing to topics..."

		for (size in ['thumb', 'full']) {
			for (filterName in Filter.ALL.name) {
				println "Subscribing to hipsteroid.filter.${filterName}.$size..."
				eventBus.send('amqp_bridge.create-consumer', [exchange: 'hipsteroid.filter', routingKey: "${filterName}.$size", forward: "hipsteroid.filter.${filterName}.$size"]) { reply ->
					println "subscribed to Rabbit for hipsteroid.filter.${filterName}.$size: $reply"
				}
			}
		}
	}

	deployVerticle 'HipsteroidFilter.groovy'

}