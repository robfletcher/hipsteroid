package co.freeside.hipsteroid

import com.github.jknack.handlebars.*
import grails.converters.JSON

class LandingController {

	static layout = 'main'

	def handlebarsService

	def timeline() {
		handlebarsService.handlebars.registerHelper('friendlyTime', new Helper<String>() {
			@Override
			CharSequence apply(String context, Options options) throws IOException {
				context
			}
		})

		println 'in the landing controller...'
		def pictures = Picture.list(params)
		[pictures: JSON.parse((pictures as JSON).toString())]
	}

}
