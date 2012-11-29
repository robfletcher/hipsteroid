package co.freeside.hipsteroid

import org.apache.commons.codec.binary.Base64
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED

class ThumbnailController {

	static allowedMethods = [generate: 'POST']

	def grailsApplication
	def springSecurityService
	def vertx

	def beforeInterceptor = {
		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			false
		} else {
			true
		}
	}

	def generate() {

		def replyAddress = params.address

		def filters = ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']
		filters.each { filterName ->

			def filterAddress = "hipsteroid.filter.${filterName}.thumb"
			println "sending ${params.image.size} to ${filterAddress} on $vertx"
			vertx.eventBus.send(filterAddress, new Buffer(params.image.bytes)) { reply ->
				println "Got response from $filterAddress"

				def buffer = new StringBuilder() << 'data:image/jpeg;base64,' << new String(Base64.encodeBase64(reply.body.bytes))

				def message = [
						filter: filterName,
						thumbnail: buffer.toString()
				]
				println "Sending $message.filter thumbnail to $replyAddress"
				vertx.eventBus.send(replyAddress, message)
			}
		}

		render status: SC_ACCEPTED
	}

}
