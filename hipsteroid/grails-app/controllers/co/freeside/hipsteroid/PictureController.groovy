package co.freeside.hipsteroid

import grails.converters.JSON
import org.bson.types.ObjectId
import twitter4j.Twitter
import static javax.servlet.http.HttpServletResponse.*

class PictureController {

	public static final int SC_UNPROCESSABLE_ENTITY = 422
	def authService

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
		Twitter twitter = session.twitter

		render(contentType: 'application/json') {
			pictures = array {
				for (p in Picture.list(params)) {
					picture {
						url = createLink(action: 'show', id: p.id)
						uploadedBy = twitter.showUser(p.uploadedBy).screenName
						dateCreated = p.dateCreated
						lastUpdated = p.lastUpdated
					}
				}
			}
		}
	}

	def save() {

		if (!authService.isAuthenticated()) {
			response.sendError SC_UNAUTHORIZED
			return
		}

		def picture = new Picture(params)
		picture.uploadedBy = authService.currentUserId

		if (picture.save(flush: true)) {
			response.status = SC_CREATED
			def model = [id: picture.id.toString()]
			render model as JSON
		} else {
			response.status = SC_UNPROCESSABLE_ENTITY
			def model = [errors: picture.errors.allErrors.collect { message(error: it) }]
			render model as JSON
		}

	}

	def update(String id) {

		if (!authService.isAuthenticated()) {
			response.sendError SC_UNAUTHORIZED
			return
		}

		def picture = Picture.get(new ObjectId(id))

		if (!picture) {
			response.sendError SC_NOT_FOUND
			return
		} else if (authService.currentUserId != picture.uploadedBy) {
			response.sendError SC_UNAUTHORIZED
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

		if (!authService.isAuthenticated()) {
			response.sendError SC_UNAUTHORIZED
			return
		}

		def picture = Picture.get(new ObjectId(id))

		if (!picture) {
			response.sendError SC_NOT_FOUND
			return
		} else if (authService.currentUserId != picture.uploadedBy) {
			response.sendError SC_UNAUTHORIZED
			return
		}

		picture.delete()

		response.status = SC_ACCEPTED
		def model = [id: picture.id.toString()]
		render model as JSON

	}

}
