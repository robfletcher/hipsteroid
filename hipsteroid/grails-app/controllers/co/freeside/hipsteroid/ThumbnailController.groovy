package co.freeside.hipsteroid

import co.freeside.hipsteroid.auth.Role
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

@Secured(Role.USER)
class ThumbnailController {

	static allowedMethods = [show: ['GET', 'HEAD'], generate: 'POST']

	def imageThumbnailService

	def show(String filename) {

		def thumbnail = imageThumbnailService.getThumbnail(filename)

		if (thumbnail) {
			response.contentType = 'image/jpeg' // TODO: depends on image
			response.contentLength = thumbnail.size
			response.outputStream << thumbnail.inputStream
		} else {
			render status: SC_NOT_FOUND
		}

	}

	def generate() {

		def thumbnails = imageThumbnailService.generatePreviews(params.image)

		def model = thumbnails.collect {
			[thumbnail: createLink(action: 'show', params: [filename: it.filename])]
		}
		render model as JSON

	}

}
