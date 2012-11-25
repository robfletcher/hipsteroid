import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import grails.util.Environment
import org.vertx.groovy.core.Vertx
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*

class BootStrap {

	def fixtureLoader

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
		vertx.createSockJSServer(httpServer).installApp(prefix: '/events') { sock ->
			sock.dataHandler { buff ->
				sock << buff
			}
		}

		httpServer.listen(8585)
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
