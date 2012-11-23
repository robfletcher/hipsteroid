package co.freeside.hipsteroid

import grails.converters.JSON
import org.bson.types.ObjectId
import static javax.servlet.http.HttpServletResponse.*
import static org.codehaus.groovy.grails.web.servlet.HttpHeaders.ACCEPT

class PictureController {

	public static final int SC_UNPROCESSABLE_ENTITY = 422

	static allowedMethods = [show: ['GET', 'HEAD'], list: ['GET', 'HEAD'], save: 'POST', update: 'PUT', delete: 'DELETE']

	def springSecurityService

	def show(String id) {

		def picture = Picture.get(new ObjectId(id))

		if (picture) {
			response.contentType = 'image/jpeg' // TODO: depends on image
			response.contentLength = picture.file.length()
			response.outputStream << picture.file.bytes
		} else {
			render status: SC_NOT_FOUND
		}

	}

	def list() {
		render(contentType: 'application/json') {
			array {
				for (p in Picture.list(params)) {
					picture {
						id = p.id.toString()
						url = createLink(action: 'show', id: p.id)
						uploadedBy = {
							screenName = p.uploadedBy.username
//							profileImageURL = user.profileImageURL
						}
						dateCreated = p.dateCreated
						lastUpdated = p.lastUpdated
					}
				}
			}
		}
	}

	def save() {

		// TODO: replace with annotation
		if (!springSecurityService.isLoggedIn()) {
			render status: SC_UNAUTHORIZED
			return
		}

		def picture = new Picture(params)
		picture.uploadedBy = springSecurityService.currentUser

		if (picture.save(flush: true)) {
			response.status = SC_CREATED
			response.contentType = request.getHeaders(ACCEPT).any { it == 'application/json' } ? 'application/json' : 'text/plain'
			def model = [[
					id: picture.id.toString(),
					name: params.image.originalFilename,
					url: createLink(action: 'show', id: picture.id),
					thumbnail_url:  createLink(action: 'show', id: picture.id),
					delete_url: createLink(action: 'delete', id: picture.id),
					delete_type: 'DELETE'
			]]
			render model as JSON
		} else {
			response.status = SC_UNPROCESSABLE_ENTITY
			def model = [errors: picture.errors.allErrors.collect { message(error: it) }]
			render model as JSON
		}

	}

	def update(String id) {

		// TODO: replace with annotation
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

		// TODO: replace with annotation
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
