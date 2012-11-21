package co.freeside.hipsteroid

import javax.servlet.http.HttpServletResponse
import grails.converters.JSON
import grails.test.mixin.*
import org.bson.types.ObjectId
import spock.lang.*
import twitter4j.*
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

@TestFor(PictureController)
@Mock(Picture)
class PictureControllerSpec extends Specification {

	@Shared Collection<File> jpgImages = ['gibson', 'manhattan', 'martini', 'oldfashioned'].collect {
		new File(PictureSpec.getResource("/${it}.jpg").toURI())
	}

	@Shared File tmpDir = new File(System.properties.'java.io.tmpdir')
	@Shared File imageRoot = new File(tmpDir, 'PictureSpec')

	@Shared User user = [
			getId: {-> 1L },
			getScreenName: {-> 'hipsterdevstack' }
	] as User

	void setupSpec() {
		imageRoot.mkdirs()
		grailsApplication.config.hipsteroid.image.root = imageRoot
	}

	void cleanupSpec() {
		imageRoot.deleteDir()
	}

	void setup() {
		HttpServletResponse.metaClass.getContentAsJSON = {->
			println delegate.contentAsString
			JSON.parse(delegate.contentAsString)
		}

		session.twitter = Mock(Twitter) {
			showUser(user.id) >> user
		}
	}

	void cleanup() {
	}

	void 'can show a picture'() {

		given:
		def picture = new Picture(image: jpgImages[0].bytes, uploadedBy: user.id)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

		when:
		controller.show(picture.id)

		then:
		response.contentType == 'image/jpeg'
		response.contentLength == jpgImages[0].length()
		response.contentAsByteArray == jpgImages[0].bytes

	}

	void 'responds with a 404 when an invalid image id is used for show'() {

		when:
		controller.show(new ObjectId())

		then:
		response.status == SC_NOT_FOUND

	}

	void 'can list pictures'() {

		given:
		def pictures = jpgImages.collect {
			def picture = new Picture(image: it.bytes, uploadedBy: user.id)
			picture.grailsApplication = grailsApplication
			picture.save(failOnError: true, flush: true)
		}

		when:
		controller.list()

		then:
		def json = response.contentAsJSON
		json.pictures.size() == jpgImages.size()
		json.pictures.every { it.uploadedBy == user.screenName }
		json.pictures[0].url == "/picture/show/${pictures[0].id}"
		json.pictures[0].dateCreated == pictures[0].dateCreated.format("yyyy-MM-dd'T'HH:mm:ss'Z'")

	}

}
