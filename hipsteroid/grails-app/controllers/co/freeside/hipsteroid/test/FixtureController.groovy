package co.freeside.hipsteroid.test

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

	def fixtureLoader

	def load(String name) {

		fixtureLoader.load(name)

		render status: SC_CREATED

	}

}
