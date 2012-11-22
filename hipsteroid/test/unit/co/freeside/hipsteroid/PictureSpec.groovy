package co.freeside.hipsteroid

import grails.test.mixin.TestFor
import spock.lang.*
import twitter4j.User
import static org.apache.commons.io.FileUtils.checksumCRC32

@TestFor(Picture)
class PictureSpec extends Specification {

	@Shared File jpgImage = new File(PictureSpec.getResource('/manhattan.jpg').toURI())
	@Shared File jpgImage2 = new File(PictureSpec.getResource('/oldfashioned.jpg').toURI())
	@Shared File tmpDir = new File(System.properties.'java.io.tmpdir')
	@Shared File imageRoot = new File(tmpDir, 'PictureSpec')
	@Shared User user = [getId: {-> 1L }] as User

	void setupSpec() {
		imageRoot.mkdirs()
	}

	void setup() {
		Picture.metaClass.imageRoot = {-> imageRoot }
	}

	void cleanupSpec() {
		imageRoot.deleteDir()
	}

	void 'when a picture is created the image file is saved'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)

		when:
		picture.save(failOnError: true, flush: true)

		then:
		picture.file.isFile()
		checksumCRC32(picture.file) == checksumCRC32(jpgImage)

	}

	void 'when a picture is loaded the image file can be retrieved'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.save(failOnError: true, flush: true)

		when:
		def file = Picture.get(picture.id).file

		then:
		file.isFile()
		checksumCRC32(file) == checksumCRC32(jpgImage)

	}

	void 'a picture can be updated with new image data'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.save(failOnError: true, flush: true)

		when:
		picture.image = jpgImage2.bytes
		picture.save(flush: true)

		then:
		picture.checksum != old(picture.checksum)
		picture.file.bytes == jpgImage2.bytes

	}

	void 'when a picture is deleted the image file is wiped from disk'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.save(failOnError: true, flush: true)

		when:
		picture.delete(flush: true)

		then:
		!picture.file.isFile()

	}

	@Ignore('not possible to check this in a unit test')
	void 'cannot change the uploading user of a picture'() {

		given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user.id)
		picture.save(failOnError: true, flush: true)

		when:
		picture.uploadedBy++
		picture.save()

		then:
		picture.refresh().uploadedBy == old(picture.uploadedBy)

	}

}
