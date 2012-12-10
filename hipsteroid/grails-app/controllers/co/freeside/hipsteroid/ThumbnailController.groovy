package co.freeside.hipsteroid

import grails.plugins.springsecurity.Secured
import org.springframework.web.multipart.MultipartFile
import static co.freeside.hipsteroid.auth.Role.USER
import static javax.servlet.http.HttpServletResponse.*

class ThumbnailController {

	static allowedMethods = [generate: 'POST']

	def springSecurityService

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

	@Secured(USER)
	def generate() {

		MultipartFile image = params.image
		def replyAddress = params.address

		def filters = ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']
		filters.each { filterName ->

			def filterAddress = "${filterName}.thumb"

			rabbitSend filterAddress, image.bytes
		}

		render status: SC_ACCEPTED
	}

}
