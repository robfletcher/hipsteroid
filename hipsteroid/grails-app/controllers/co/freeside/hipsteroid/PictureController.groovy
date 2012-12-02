package co.freeside.hipsteroid

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.validation.Validateable
import org.bson.types.ObjectId
import org.vertx.groovy.core.buffer.Buffer
import static co.freeside.hipsteroid.auth.Role.USER
import static javax.servlet.http.HttpServletResponse.*

class PictureController {

	public static final int SC_UNPROCESSABLE_ENTITY = 422

	static allowedMethods = [show: ['GET', 'HEAD'], list: ['GET', 'HEAD'], save: 'POST', update: 'PUT', delete: 'DELETE']

	def springSecurityService
	def vertx

	def show(String id) {

		def picture = Picture.get(new ObjectId(id))

		if (picture) {
			withFormat {
				jpg {
					response.contentType = 'image/jpeg'
					response.contentLength = picture.image.length
					response.outputStream << picture.image
				}
				json {
					render picture as JSON
				}
			}
		} else {
			render status: SC_NOT_FOUND
		}

	}

	def list() {
		def pictures = Picture.list(params)
		render pictures as JSON
	}

	def beforeInterceptor = [action: this.&bindUploadCommand, only: 'save']

	def bindUploadCommand() {
		if (request.format == 'json') {
			params.filter = request.JSON.filter
			params.image = request.JSON.image
		}
		true
	}

	@Secured(USER)
	def save(UploadPictureCommand command) {

		if (!command.validate()) {
			println command.errors.allErrors.code
			response.status = SC_UNPROCESSABLE_ENTITY
			def model = [errors: command.errors.allErrors.collect { message(error: it) }]
			render model as JSON
			return
		}

		def picture = new Picture()
		picture.uploadedBy = springSecurityService.currentUser

		vertx.eventBus.send("hipsteroid.filter.${command.filter}.full", new Buffer(command.decodedImage)) { reply ->
			def replyBuffer = new Buffer(reply.body)

			picture.image = replyBuffer.bytes
			if (picture.save(flush: true)) {
				log.info 'image processed successfully'
			} else {
				log.error picture.errors.allErrors.collect { message(error: it) }
			}
		}

		render status: SC_ACCEPTED

	}

	@Secured(USER)
	def update(String id) {

		def picture = Picture.get(new ObjectId(id))

		if (!picture) {
			render status: SC_NOT_FOUND
			return
		} else if (springSecurityService.currentUser != picture.uploadedBy) {
			render status: SC_FORBIDDEN
			return
		}

		picture.properties = params

		if (picture.save(flush: true)) {
			response.status = SC_OK
			def model = [id: picture.id.toString()]
			render model as JSON
		} else {
			response.status = SC_UNPROCESSABLE_ENTITY
			def model = [errors: picture.errors.allErrors.collect { message(error: it) }]
			render model as JSON
		}

	}

	@Secured(USER)
	def delete(String id) {

		def picture = Picture.get(new ObjectId(id))

		if (!picture) {
			render status: SC_NOT_FOUND
			return
		} else if (springSecurityService.currentUser != picture.uploadedBy) {
			render status: SC_FORBIDDEN
			return
		}

		picture.delete()

		response.status = SC_ACCEPTED
		def model = [id: picture.id.toString()]
		render model as JSON

	}

}

@Validateable
class UploadPictureCommand {

	String filter
	String image

	static constraints = {
		filter nullable: false, inList: ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']
		image nullable: false, validator: { it.startsWith('data:image/jpeg;base64,') }
	}

	byte[] getDecodedImage() {
		image.decodeDataUrl()
	}

}