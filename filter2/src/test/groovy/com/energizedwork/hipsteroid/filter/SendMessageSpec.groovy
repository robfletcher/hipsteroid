package com.energizedwork.hipsteroid.filter

import groovy.transform.CompileStatic
import spock.lang.*

class SendMessageSpec extends Specification {

	private static final String QUEUE_NAME = "hello"

	@Timeout(1)
	void 'can send a message to a receiver'() {
		given: 'a receiver'
		def receiver = new Receiver(QUEUE_NAME, new ReverserHandler())

		and: 'a sender'
		def sender = new Sender(QUEUE_NAME)

		when: 'the receiver is started'
		Thread.start {
			receiver.start()
		}

		and: 'a message is sent'
		def message = "Hello World!"
		def reply = sender.call(message.bytes)

		then: 'the response is received'
		reply == message.reverse().bytes

		cleanup:
		receiver?.close()
		sender?.close()
	}

}

@CompileStatic
class ReverserHandler implements MessageHandler {

	@Override
	byte[] onMessage(byte[] message) {
		new String(message).reverse().bytes
	}
}


