package co.freeside.hipsteroid

import org.springframework.web.multipart.MultipartFile
import org.vertx.groovy.core.buffer.Buffer
import static javax.servlet.http.HttpServletResponse.*

class ThumbnailController {

	static allowedMethods = [generate: 'POST']

	def springSecurityService
	def vertx

	def beforeInterceptor = {
		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			return false
		}

		MultipartFile image = params.image
		if (!image) {
			render status: SC_BAD_REQUEST
			return false
		} else if (image.contentType != 'image/jpeg') {
			render status: SC_UNSUPPORTED_MEDIA_TYPE
			return false
		}

		return true
	}

	def generate() {

		MultipartFile image = params.image
		def replyAddress = params.address

		def filters = ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']
		filters.each { filterName ->

			def filterAddress = "hipsteroid.filter.${filterName}.thumb"
			vertx.eventBus.send(filterAddress, new Buffer(image.bytes)) { reply ->
				def message = [
						filter: filterName,
						thumbnail: DataUrlCodec.encode(reply.body.bytes, 'image/jpeg')
				]
				vertx.eventBus.send replyAddress, message
			}
		}

		render status: SC_ACCEPTED
	}

}
