import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import grails.util.Environment
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*

class BootStrap {

	def fixtureLoader
	def uploadService

	def init = { servletContext ->

		ensureDefaultUsersAndRolesExist()
		loadDefaultTestData()

		startSocketServer()

	}

	def destroy = {
	}

	private void startSocketServer() {
		def vertx = Vertx.newVertx()
		def httpServer = vertx.createHttpServer()

		def inboundPermitted = [
		        [address: 'upload:start'],
		        [address: 'upload:upload']
		]
		def outboundPermitted = [[:]]

		vertx.createSockJSServer(httpServer).bridge(prefix: '/eventbus', inboundPermitted, outboundPermitted)
		httpServer.listen(8585)

		uploadService.register vertx.eventBus
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
		if (Environment.current in [DEVELOPMENT, TEST] && Picture.count() == 0) {
			fixtureLoader.load 'pictures/cocktails'
		}
	}

}
