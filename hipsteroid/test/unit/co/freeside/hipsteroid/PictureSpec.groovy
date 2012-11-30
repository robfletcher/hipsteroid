package co.freeside.hipsteroid

import co.freeside.hipsteroid.auth.User
import grails.test.mixin.*
import spock.lang.*
import static org.apache.commons.io.FileUtils.checksumCRC32

@TestFor(Picture)
@Mock(ImageData)
class PictureSpec extends Specification {

	@Shared File jpgImage = new File(PictureSpec.getResource('/manhattan.jpg').toURI())
	@Shared File jpgImage2 = new File(PictureSpec.getResource('/oldfashioned.jpg').toURI())
	@Shared User user = new User(username: 'roundhouse')

	void 'when a picture is created the image data is saved'() {

	given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user)

	when:
		picture.save(failOnError: true, flush: true)

	then:
		picture.imageData
		picture.imageData.checksumCRC32() == checksumCRC32(jpgImage)

	}

	void 'when a picture is loaded the image data can be retrieved'() {

	given:
		def id = Picture.withNewSession {
			def picture = new Picture(image: jpgImage.bytes, uploadedBy: user)
			picture.save(failOnError: true, flush: true).id
		}

	when:
		def picture = Picture.get(id)

	then:
		picture.imageData
		picture.imageData.checksumCRC32() == checksumCRC32(jpgImage)

	}

	void 'a picture can be updated with new image data'() {

	given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user)
		picture.save(failOnError: true, flush: true)

	when:
		picture.image = jpgImage2.bytes
		picture.save(flush: true)

	then:
		picture.imageData.checksumCRC32() != old(picture.imageData.checksumCRC32())
		picture.imageData.checksumCRC32() == checksumCRC32(jpgImage2)

	}

	void 'when a picture is deleted the image data is also deleted'() {

	given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user)
		picture.save(failOnError: true, flush: true)

	when:
		picture.delete(flush: true)

	then:
		ImageData.count() == old(ImageData.count()) - 1
		!ImageData.findByPicture(picture)
		ImageData.countByPicture(picture) == 0

	}

	@Ignore('not possible to check this in a unit test')
	void 'cannot change the uploading user of a picture'() {

	given:
		def picture = new Picture(image: jpgImage.bytes, uploadedBy: user)
		picture.save(failOnError: true, flush: true)

	when:
		picture.uploadedBy++
		picture.save()

	then:
		picture.refresh().uploadedBy == old(picture.uploadedBy)

	}

}
