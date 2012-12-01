package co.freeside.hipsteroid

import grails.converters.JSON
import org.bson.types.ObjectId
import org.vertx.groovy.core.buffer.Buffer
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

	def save() {

		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			return
		}

		def picture = new Picture()
		picture.uploadedBy = springSecurityService.currentUser

		if (picture.save(flush: true)) {
			def id = picture.id
			vertx.eventBus.send("hipsteroid.filter.${request.JSON.filter}.full", new Buffer(request.JSON.image[23..-1].decodeBase64())) { reply ->
				def replyBuffer = new Buffer(reply.body)
				def imageData = new ImageData(data: replyBuffer.bytes, picture: Picture.get(id))
				if (imageData.save(flush: true)) {
					log.info 'image processed successfully'
				} else {
					log.error picture.errors.allErrors.collect { message(error: it) }
				}
			}

			response.status = SC_CREATED
			render picture as JSON
		} else {
			response.status = SC_UNPROCESSABLE_ENTITY
			def model = [errors: picture.errors.allErrors.collect { message(error: it) }]
			render model as JSON
		}

	}

	def update(String id) {

		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			return
		}

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

	def delete(String id) {

		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			return
		}

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
