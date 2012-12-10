import org.cloudfoundry.runtime.env.CloudEnvironment
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.http.*
import org.vertx.mods.formupload.MultipartRequest;
import org.vertx.mods.formupload.Upload;
import org.vertx.java.core.Handler

def cloudEnv = new CloudEnvironment()

def eventBus = vertx.eventBus

// Configuration for the web server
def webServerConf = [

		// Normal web server stuff

		port: (cloudEnv.getValue('VCAP_APP_PORT') ?: '8080') as int,
		host: cloudEnv.getValue('VCAP_APP_HOST') ?: 'localhost',
		/* ssl: true, */

		// Configuration for the event bus client side bridge
		// This bridges messages from the client side to the server side event bus
		bridge: true,

		// This defines which messages from the client we will let through
		// to the server side
		inbound_permitted: [
				// Allow calls to filters
				[
						address_re: /hipsteroid\.filter\.\w+\.\w+/
				],
		],

		// This defines which messages from the server we will let through to the client
		outbound_permitted: [
				[:]
		]
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

	// Start the web server, with the config we defined above

//	deployModule('vertx.web-server-v1.0', webServerConf)

	deployVerticle 'HipsteroidFilter.groovy'

}