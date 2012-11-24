package co.freeside.hipsteroid

import javax.servlet.http.HttpServletResponse
import co.freeside.hipsteroid.auth.User
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.*
import org.bson.types.ObjectId
import org.springframework.mock.web.MockMultipartFile
import spock.lang.*
import static co.freeside.hipsteroid.PictureController.SC_UNPROCESSABLE_ENTITY
import static javax.servlet.http.HttpServletResponse.*

@TestFor(PictureController)
@Mock(Picture)
@Unroll
class PictureControllerSpec extends Specification {

	@Shared Collection<File> jpgImages = ['gibson', 'manhattan', 'martini', 'oldfashioned'].collect {
		new File(PictureSpec.getResource("/${it}.jpg").toURI())
	}

	@Shared File tmpDir = new File(System.properties.'java.io.tmpdir')
	@Shared File imageRoot = new File(tmpDir, 'PictureSpec')

	User user1 = new User(username: 'roundhouse')
	User user2 = new User(username: 'ponytail')

	void setupSpec() {
		imageRoot.mkdirs()
	}

	void cleanupSpec() {
		imageRoot.deleteDir()
	}

	void setup() {
		Picture.metaClass.imageRoot = {-> imageRoot }

		HttpServletResponse.metaClass.getContentAsJSON = {->
			JSON.parse(delegate.contentAsString)
		}

		controller.springSecurityService = Mock(SpringSecurityService)
	}

	void cleanup() {
	}

	void 'can show a picture'() {

		given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

		when:
		controller.show picture.id.toString()

		then:
		response.contentType == 'image/jpeg'
		response.contentLength == jpgImages[0].length()
		response.contentAsByteArray == jpgImages[0].bytes

	}

	void 'can list pictures'() {

		given:
		def pictures = jpgImages.collect {
			def picture = new Picture(image: it.bytes, uploadedBy: user1)
			picture.grailsApplication = grailsApplication
			picture.save(failOnError: true, flush: true)
		}

		when:
		controller.list()

		then:
		def json = response.contentAsJSON
		json.size() == jpgImages.size()
		json[0].uploadedBy.id == user1.id
		json[0].uploadedBy.username == user1.username
		json[0].url == "/picture/show/${pictures[0].id}"
		json[0].dateCreated == pictures[0].dateCreated.format("yyyy-MM-dd'T'HH:mm:ss'Z'")

	}

	void 'authenticated user can create a picture'() {

		given:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

		and:
		request.addFile new MockMultipartFile('image', jpgImages[0].bytes)

		when:
		controller.save()

		then:
		response.status == SC_CREATED
		String id = response.contentAsJSON[0].id

		and:
		Picture.count() == old(Picture.count()) + 1
		def picture = Picture.get(new ObjectId(id))
		picture.file.bytes == jpgImages[0].bytes
		picture.uploadedBy == user1

	}

	void 'save responds with 422 if upload data is invalid or missing'() {

		given:
		controller.springSecurityService.isLoggedIn() >> true
		controller.springSecurityService.currentUser >> user1

		when:
		controller.save()

		then:
		response.status == SC_UNPROCESSABLE_ENTITY
		response.contentAsJSON.errors[0] == "Property [image] of class [$Picture] cannot be null"

	}

	void 'uploader can update a picture'() {

		given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

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
		picture.file.bytes == jpgImages[1].bytes

	}

	void 'uploader can delete a picture'() {

		given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

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
		!picture.file.isFile()

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

	void '#action responds with a #httpStatus if no user is authenticated'() {

		given:
		controller.springSecurityService.isLoggedIn() >> false

		when:
		controller.invokeMethod(action, args as Object[])

		then:
		response.status == httpStatus

		where:
		action << ['save', 'update', 'delete']
		args << [[], [new ObjectId().toString()]]
		httpStatus = SC_UNAUTHORIZED

	}

	void '#action responds with #httpStatus if wrong user is logged in'() {

		given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user1)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

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
