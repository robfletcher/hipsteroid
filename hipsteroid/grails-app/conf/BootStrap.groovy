import co.freeside.hipsteroid.*
import grails.util.Environment
import static grails.util.Environment.*

class BootStrap {

	def fixtureLoader

	def init = { servletContext ->

		if (Environment.current in [DEVELOPMENT, TEST] && Picture.count() == 0) {

			fixtureLoader.load 'pictures/cocktails'

		}

	}

	def destroy = {
	}

}
