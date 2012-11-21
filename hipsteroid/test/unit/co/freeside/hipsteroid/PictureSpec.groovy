package co.freeside.hipsteroid

import grails.test.mixin.TestFor
import spock.lang.*
import twitter4j.User
import static org.apache.commons.io.FileUtils.checksumCRC32

@TestFor(Picture)
class PictureSpec extends Specification {

	@Shared File jpgImage = new File(PictureSpec.getResource('/manhattan.jpg').toURI())
	@Shared File tmpDir = new File(System.properties.'java.io.tmpdir')
	@Shared File imageRoot = new File(tmpDir, 'PictureSpec')
	@Shared User user = Mock(User)

	void setupSpec() {
		imageRoot.mkdirs()
		grailsApplication.config.hipsteroid.image.root = imageRoot
	}

	void cleanupSpec() {
		imageRoot.deleteDir()
	}

	void 'when a picture is created the image file is saved'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.grailsApplication = grailsApplication

		when:
		picture.save(failOnError: true, flush: true)

		then:
		picture.file.isFile()
		checksumCRC32(picture.file) == checksumCRC32(jpgImage)

	}

	void 'when a picture is loaded the image file can be retrieved'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.grailsApplication = grailsApplication
		picture.save(failOnError: true, flush: true)

		when:
		def file = Picture.get(picture.id).file

		then:
		file.isFile()
		checksumCRC32(file) == checksumCRC32(jpgImage)

	}

}
