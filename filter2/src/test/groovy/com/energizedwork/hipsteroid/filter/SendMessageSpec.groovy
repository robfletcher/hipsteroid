package com.energizedwork.hipsteroid.filter

import com.rabbitmq.client.*
import groovy.transform.CompileStatic
import spock.lang.Specification

class SendMessageSpec extends Specification {

	private static final String QUEUE_NAME = "hello"

	void 'can send a message to a receiver'() {
		given: 'a receiver'
		def receiver = new Receiver()

		and: 'a sender'
		def sender = new Sender(QUEUE_NAME)

		when: 'the receiver is started'
		Thread.start {
			receiver.start()
		}

		and: 'a message is sent'
		def message = "Hello World!"
		def reply = sender.call(message)

		then: 'the response is received'
		reply == message.reverse()

		cleanup:
		receiver?.close()
		sender?.close()
	}

}

/**
 * This is a dumb blocking sender which is fine for a test.
 */
@CompileStatic
class Sender {
	private Connection connection
	private Channel channel
	private String requestQueueName
	private String replyQueueName
	private QueueingConsumer consumer

	Sender(String requestQueueName) {
		this.requestQueueName = requestQueueName

		def factory = new ConnectionFactory()
		factory.host = "localhost"
		connection = factory.newConnection()
		channel = connection.createChannel()

		replyQueueName = channel.queueDeclare().queue
		consumer = new QueueingConsumer(channel)
		channel.basicConsume replyQueueName, true, consumer
	}

	String call(String message) {
		def response = null
		def corrId = UUID.randomUUID().toString()

		def props = new AMQP.BasicProperties.Builder()
				.correlationId(corrId)
				.replyTo(replyQueueName)
				.build()

		channel.basicPublish "", requestQueueName, props, message.bytes

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery()
			if (delivery.properties.correlationId == corrId) {
				response = new String(delivery.body)
				break
			}
		}

		return response
	}

	void close() {
		connection.close()
	}
}