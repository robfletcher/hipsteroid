package co.freeside.hipsteroid

import org.bson.types.ObjectId
import twitter4j.Twitter
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

class PictureController {

	def show(ObjectId id) {

		def picture = Picture.get(id)

		if (picture) {
			response.contentType = 'image/jpeg'
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
					}
				}
			}
		}
	}

}
