package co.freeside.hipsteroid

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.EventBus
import spock.lang.*
import static javax.servlet.http.HttpServletResponse.*

@TestFor(ThumbnailController)
class ThumbnailControllerSpec extends Specification {

	@Shared File jpgImage = new File(PictureSpec.getResource('/manhattan.jpg').toURI())

	def eventBus = Mock(EventBus)

	void setup() {
		controller.springSecurityService = Mock(SpringSecurityService)
		controller.vertx = Mock(Vertx) {
			getEventBus() >> eventBus
		}
	}

	void 'rejects requests from non-authenticated users'() {

	given:
		controller.springSecurityService.isLoggedIn() >> false

	expect:
		!controller.beforeInterceptor()

	and:
		response.status == SC_UNAUTHORIZED

	}

	void 'rejects requests without an image'() {

	given:
		controller.springSecurityService.isLoggedIn() >> true

	expect:
		!controller.beforeInterceptor()

	and:
		response.status == SC_BAD_REQUEST

	}

	void 'rejects requests with anything other than jpeg data'() {

	given:
		controller.springSecurityService.isLoggedIn() >> true

	and:
		params.image = new MockMultipartFile('image', jpgImage.name, 'text/plain', jpgImage.name.bytes)

	expect:
		!controller.beforeInterceptor()

	and:
		response.status == SC_UNSUPPORTED_MEDIA_TYPE

	}

	void 'sends an uploaded image to each filter address'() {

	given:
		params.address = 'hipsteroid.filter.thumb.abc123'
		params.image = new MockMultipartFile('image', jpgImage.name, 'image/jpeg', jpgImage.bytes)

	when:
		controller.generate()

	then:
		1 * eventBus.send('hipsteroid.filter.gotham.thumb', { it.bytes == jpgImage.bytes }, _ as Closure)
		1 * eventBus.send('hipsteroid.filter.toaster.thumb', { it.bytes == jpgImage.bytes }, _ as Closure)
		1 * eventBus.send('hipsteroid.filter.lomo.thumb', { it.bytes == jpgImage.bytes }, _ as Closure)
		1 * eventBus.send('hipsteroid.filter.nashville.thumb', { it.bytes == jpgImage.bytes }, _ as Closure)
		1 * eventBus.send('hipsteroid.filter.kelvin.thumb', { it.bytes == jpgImage.bytes }, _ as Closure)

	}

	void 'sends generated thumbnails back to the page via the event bus'() {

	given:
		eventBus.send({ it ==~ /hipsteroid\.filter\.\w+\.thumb/ }, _, _ as Closure) >> { address, message, replyHandler ->
			def reply = [body: "Reply from $address".toString()]
			replyHandler reply
			null
		}

	and:
		params.address = 'hipsteroid.filter.thumb.abc123'
		params.image = new MockMultipartFile('image', jpgImage.name, 'image/jpeg', jpgImage.bytes)

	when:
		controller.generate()

	then:
		1 * eventBus.send(params.address, [filter: 'gotham', thumbnail: "data:image/jpeg;base64,${'Reply from hipsteroid.filter.gotham.thumb'.encodeAsBase64()}"])
		1 * eventBus.send(params.address, [filter: 'toaster', thumbnail: "data:image/jpeg;base64,${'Reply from hipsteroid.filter.toaster.thumb'.encodeAsBase64()}"])
		1 * eventBus.send(params.address, [filter: 'lomo', thumbnail: "data:image/jpeg;base64,${'Reply from hipsteroid.filter.lomo.thumb'.encodeAsBase64()}"])
		1 * eventBus.send(params.address, [filter: 'nashville', thumbnail: "data:image/jpeg;base64,${'Reply from hipsteroid.filter.nashville.thumb'.encodeAsBase64()}"])
		1 * eventBus.send(params.address, [filter: 'kelvin', thumbnail: "data:image/jpeg;base64,${'Reply from hipsteroid.filter.kelvin.thumb'.encodeAsBase64()}"])

	}

}