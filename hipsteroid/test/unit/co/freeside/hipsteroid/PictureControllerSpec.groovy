package co.freeside.hipsteroid

import javax.servlet.http.HttpServletResponse
import co.freeside.hipsteroid.auth.User
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin
import org.bson.types.ObjectId
import org.springframework.mock.web.MockMultipartFile
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.EventBus
import spock.lang.*
import static co.freeside.hipsteroid.PictureController.SC_UNPROCESSABLE_ENTITY
import static javax.servlet.http.HttpServletResponse.*

@TestMixin(GrailsUnitTestMixin)
@TestFor(PictureController)
@Mock([Picture, ImageData, User])
@Unroll
class PictureControllerSpec extends Specification {

	@Shared Collection<File> jpgImages = ['gibson', 'manhattan', 'martini', 'oldfashioned'].collect {
		new File(PictureSpec.getResource("/${it}.jpg").toURI())
	}

	User user1 = new User(username: 'roundhouse')
	User user2 = new User(username: 'ponytail')

	def eventBus = Mock(EventBus)

	void setup() {
		mockCodec DataUrlCodec
		byte[].metaClass.encodeAsDataUrl = { String mimeType ->
			DataUrlCodec.encode delegate, mimeType
		}

		JSON.registerObjectMarshaller(ObjectId) {
			it.toString()
		}

		HttpServletResponse.metaClass.getContentAsJSON = {->
			JSON.parse(delegate.contentAsString)
		}

		def mockSpringSecurityService = Mock(SpringSecurityService)

		controller.springSecurityService = mockSpringSecurityService

		[user1, user2].each {
			it.springSecurityService = mockSpringSecurityService
			it.save(validate: false, failOnError: true)
		}

		controller.vertx = Mock(Vertx) {
			getEventBus() >> eventBus
		}
	}

	void cleanup() {
	}

	void 'can show a picture as JPG'() {

	given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1).save(failOnError: true, flush: true)

	and:
		request.format = 'jpg'

	when:
		controller.show picture.id.toString()

	then:
		response.contentType == 'image/jpeg'
		response.contentLength == jpgImages[0].length()
		response.contentAsByteArray == jpgImages[0].bytes

	}

	void 'can show a picture as JSON'() {

	given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1).save(failOnError: true, flush: true)

	and:
		response.format = 'json'

	when:
		controller.show picture.id.toString()

	then:
		response.contentType == 'application/json;charset=UTF-8'
		response.contentAsJSON.id == picture.id.toString()

	}

	void 'can list pictures'() {

	given:
		def pictures = jpgImages.collect {
			new Picture(image: it.bytes, uploadedBy: user1).save(failOnError: true, flush: true)
		}

	when:
		controller.list()

	then:
		def json = response.contentAsJSON
		json.size() == jpgImages.size()
		json[0].id == pictures[0].id.toString()
		json[0].uploadedBy.id == user1.id.toString()
		json[0].dateCreated == pictures[0].dateCreated.format("yyyy-MM-dd'T'HH:mm:ss'Z'")

	}

	void 'authenticated user can create a picture'() {

	given:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

	and:
		def command = new UploadPictureCommand(filter: 'lomo', image: jpgImages[0].bytes.encodeAsDataUrl('image/jpeg'))

	when:
		controller.save command

	then:
		1 * eventBus.send('hipsteroid.filter.lomo.full', _ as Buffer, _ as Closure) >> { address, buffer, callback ->
			assert buffer.bytes == jpgImages[0].bytes
			callback(body: 'filtered image'.bytes)
		}

	and:
		response.status == SC_ACCEPTED

	and:
		Picture.count() == old(Picture.count()) + 1
		def picture = Picture.first() // default sort by descending dateCreated so this will get most recent
		picture.image == 'filtered image'.bytes
		picture.uploadedBy == user1
	}

	void 'save responds with 422 if upload data is invalid or missing'() {

	given:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

	when:
		controller.save new UploadPictureCommand()

	then:
		response.status == SC_UNPROCESSABLE_ENTITY
		response.contentAsJSON.errors[0] == "Property [filter] of class [$UploadPictureCommand] cannot be null"
		response.contentAsJSON.errors[1] == "Property [image] of class [$UploadPictureCommand] cannot be null"

	and:
		0 * eventBus._

	}

	void 'uploader can update a picture'() {

	given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1).save(failOnError: true, flush: true)

	and:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

	and:
		request.addFile new MockMultipartFile('image', jpgImages[1].bytes)

	when:
		controller.update picture.id.toString()

	then:
		response.status == SC_OK

	and:
		picture.image == jpgImages[1].bytes

	}

	void 'uploader can delete a picture'() {

	given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1).save(failOnError: true, flush: true)

	and:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

	when:
		controller.delete picture.id.toString()

	then:
		response.status == SC_ACCEPTED

	and:
		Picture.count() == old(Picture.count()) - 1
		Picture.get(picture.id) == null

	and:
		ImageData.countByPicture(picture) == 0

	}

	void '#action responds with a #httpStatus if picture id is incorrect'() {

	given:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

	when:
		controller."$action" new ObjectId().toString()

	then:
		response.status == httpStatus

	where:
		action << ['show', 'update', 'delete']
		httpStatus = SC_NOT_FOUND

	}

	@Ignore('handled by spring security annotation')
	void '#action responds with a #httpStatus if no user is authenticated'() {

	given:
		controller.springSecurityService.isLoggedIn() >> false

	when:
		controller.invokeMethod(action, args as Object[])

	then:
		response.status == httpStatus

	where:
		action << ['save', 'update', 'delete']
		args << [[], [new ObjectId().toString()], [new ObjectId().toString()]]
		httpStatus = SC_UNAUTHORIZED

	}

	void '#action responds with #httpStatus if wrong user is logged in'() {

	given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1).save(failOnError: true, flush: true)

	and:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user2

	when:
		controller."$action" picture.id.toString()

	then:
		response.status == httpStatus

	where:
		action << ['update', 'delete']
		httpStatus = SC_FORBIDDEN

	}

}
