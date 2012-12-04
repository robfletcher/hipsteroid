package co.freeside.hipsteroid

import grails.converters.JSON

class LandingController {

	static layout = 'main'

	def timeline() {
		def pictures = Picture.list(params)
		[pictures: JSON.parse(pictures.encodeAsJSON())]
	}

}
