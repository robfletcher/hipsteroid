package co.freeside.hipsteroid.test

import co.freeside.hipsteroid.Picture
import grails.util.Environment
import static grails.util.Environment.*
import static javax.servlet.http.HttpServletResponse.*

class FixtureController {
	
	def beforeInterceptor = {
		if (Environment.current in [DEVELOPMENT, TEST]) {
			return true
		} else {
			render status: SC_FORBIDDEN
			return false
		}
	}

	def nuke() {
		Picture.deleteAll Picture.list()
		response.status = SC_OK
	}

	def fixtureLoader

	def load(String name) {
		fixtureLoader.load(name)
		response.status = SC_CREATED
	}

}
