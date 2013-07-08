package com.energizedwork.hipsteroid.filter

import com.rabbitmq.client.*
import groovy.transform.CompileStatic
import groovy.util.logging.Log

@CompileStatic
@Log
class Receiver {

	final String queueName
	private final MessageHandler handler

	private final Channel channel
	private final Connection connection

	Receiver(String queueName, MessageHandler handler) {
		this.queueName = queueName
		this.handler = handler

		def factory = new ConnectionFactory()
		factory.host = "localhost"
		connection = factory.newConnection()
		channel = connection.createChannel()
	}

	void start() {
		channel.queueDeclare queueName, false, false, false, null
		log.info "Waiting for messages on $queueName..."

		def consumer = new QueueingConsumer(channel)
		channel.basicConsume queueName, false, consumer

		try {
			while (channel.isOpen()) {
				def delivery = consumer.nextDelivery()

				def props = delivery.properties
				def replyProps = new AMQP.BasicProperties.Builder().correlationId(props.correlationId).build()

				def reply = handler.onMessage(delivery.body)

				channel.basicPublish "", props.replyTo, replyProps, reply
				channel.basicAck delivery.envelope.deliveryTag, false
			}
		} catch (InterruptedException e) {
			log.warning "Receiver for $queueName interrupted: $e.message"
		} catch (ShutdownSignalException e) {
			log.warning "Connection for $queueName shut down: $e.message"
		} catch (ConsumerCancelledException e) {
			log.warning "Consumer for $queueName cancelled: $e.message"
		}
	}

	void close() {
		channel?.close()
		connection?.close()
	}
}
