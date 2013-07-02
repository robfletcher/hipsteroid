package com.energizedwork.hipsteroid.filter

import java.awt.*
import java.util.zip.CRC32
import spock.lang.*

class FilterHandlerSpec extends Specification {

	private static final QUEUE_NAME = "filter_q"

	void "returns filtered image data"() {
		given: "a filter"
		def handler = new FilterHandler(Filter.GOTHAM, new Dimension(100, 100))

		and: "an image"
		def image = getResource("manhattan.jpg")

		when: "the handler processes the image data"
		def result = handler.onMessage(image.bytes)

		then: "the image is filtered correctly"
		checksum(result) == checksum(getResource("manhattan-gotham.thumb.jpg"))
	}

	@Timeout(10)
	void "filters image data send via MQ"() {
		given: "a handler"
		def handler = new FilterHandler(Filter.GOTHAM, new Dimension(100, 100))

		and: "a receiver"
		def receiver = new Receiver(QUEUE_NAME, handler)

		and: "a sender"
		def sender = new Sender(QUEUE_NAME)

		and: "an image"
		def image = getResource("manhattan.jpg")

		when: "the receiver is started"
		Thread.start {
			receiver.start()
		}

		and: "image data is sent to the queue"
		def reply = sender.call(image.bytes)

		then: "the reply contains the filtered image data"
		checksum(reply) == checksum(getResource("manhattan-gotham.thumb.jpg"))

		cleanup:
		receiver?.close()
		sender?.close()
	}

	private URL getResource(String path) {
		getClass().getResource "/$path"
	}

	private static long checksum(URL file) {
		checksum file.bytes
	}

	private static long checksum(byte[] bytes) {
		def checksum = new CRC32()
		checksum.update(bytes)
		return checksum.value
	}
}
