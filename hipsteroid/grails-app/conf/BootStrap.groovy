import co.freeside.hipsteroid.*
import co.freeside.hipsteroid.auth.Role
import co.freeside.hipsteroid.auth.User
import co.freeside.hipsteroid.auth.UserRole
import grails.util.Environment
import static grails.util.Environment.*

class BootStrap {

	def fixtureLoader

	def init = { servletContext ->

		def role = Role.findOrSaveByAuthority 'ROLE_USER'
		def user = User.findOrCreateByUsername('hipsteroid')
		user.password = 'hipsteroid'
		user.enabled = true
		user.save(failOnError: true)
		UserRole.findOrSaveByUserAndRole(user, role)

		if (Environment.current in [DEVELOPMENT, TEST] && Picture.count() == 0) {

			fixtureLoader.load 'pictures/cocktails'

		}

	}

	def destroy = {
	}

}
