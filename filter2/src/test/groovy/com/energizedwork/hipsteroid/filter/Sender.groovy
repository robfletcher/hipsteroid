package com.energizedwork.hipsteroid.filter

import com.rabbitmq.client.*
import groovy.transform.CompileStatic

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

	byte[] call(byte[] message) {
		byte[] response = null
		def corrId = UUID.randomUUID().toString()

		def props = new AMQP.BasicProperties.Builder()
				.correlationId(corrId)
				.replyTo(replyQueueName)
				.build()

		channel.basicPublish "", requestQueueName, props, message

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery()
			if (delivery.properties.correlationId == corrId) {
				response = delivery.body
				break
			}
		}

		return response
	}

	void close() {
		connection.close()
	}
}
