import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import grails.util.Environment
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*

class BootStrap {

	def fixtureLoader

	def init = { servletContext ->

		ensureDefaultUsersAndRolesExist()
		loadDefaultTestData()

	}

	def destroy = {
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
