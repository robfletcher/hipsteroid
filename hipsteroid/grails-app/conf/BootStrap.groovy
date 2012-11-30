import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import grails.converters.JSON
import grails.util.Environment
import org.bson.types.ObjectId
import org.vertx.groovy.core.http.HttpServer
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*

class BootStrap {

	def grailsApplication
	def grailsLinkGenerator
	def fixtureLoader
	def vertx

	def init = { servletContext ->

		registerJsonHandlers()

		if (Environment.current in [DEVELOPMENT, TEST]) {
			ensureDefaultUsersAndRolesExist()
			loadDefaultTestData()
		}

		startEventBusBridge()

	}

	void registerJsonHandlers() {
		JSON.registerObjectMarshaller(ObjectId) {
			it.toString()
		}

		JSON.registerObjectMarshaller(Picture) {
			[
			        id: it.id,
					url: grailsLinkGenerator.link(controller: 'picture', action: 'show', id: it.id, absolute: true),
					uploadedBy: it.uploadedBy,
					dateCreated: it.dateCreated,
					lastUpdated: it.lastUpdated
			]
		}

		JSON.registerObjectMarshaller(User) {
			[
					id: it.id,
					username: it.username
			]
		}
	}

	def destroy = {
	}

	private void startEventBusBridge() {
		HttpServer httpServer = vertx.createHttpServer()

		def inboundPermitted = []
		def outboundPermitted = [[address_re: /hipsteroid\.filter\..+/]]

		vertx.createSockJSServer(httpServer).bridge(prefix: '/eventbus', inboundPermitted, outboundPermitted)

		def port = config.vertx.eventBus.bridge.port
		httpServer.listen(port)
		println "vertx listening on port $port"
	}

	private void ensureDefaultUsersAndRolesExist() {
		def role = Role.findOrSaveByAuthority USER
		def user = User.findOrCreateByUsername 'hipsteroid'
		user.password = 'hipsteroid'
		user.enabled = true
		user.save failOnError: true
		UserRole.findOrSaveByUserAndRole user, role
	}

	private void loadDefaultTestData() {
		if (Picture.count() == 0) {
			fixtureLoader.load 'pictures/cocktails'
		}
	}

	private ConfigObject getConfig() {
		grailsApplication.config
	}

}
