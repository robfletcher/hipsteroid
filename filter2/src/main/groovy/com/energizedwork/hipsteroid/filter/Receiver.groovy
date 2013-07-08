package com.energizedwork.hipsteroid.filter

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
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
	private boolean stopped = false

	private final AtomicInteger counter = new AtomicInteger()
	private final CountDownLatch stopLatch = new CountDownLatch(1)

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

		while (!stopped && channel.isOpen()) {
			def delivery = consumer.nextDelivery(50)

			if (delivery) {
				counter.incrementAndGet()

				def props = delivery.properties
				def replyProps = new AMQP.BasicProperties.Builder().correlationId(props.correlationId).build()

				def reply = handler.onMessage(delivery.body)

				channel.basicPublish "", props.replyTo, replyProps, reply
				channel.basicAck delivery.envelope.deliveryTag, false
			}
		}
		stopLatch.countDown()
	}

	void close() {
		stopped = true
		stopLatch.await()
		channel?.close()
		connection?.close()
	}

	int getCount() {
		counter.get()
	}

	private String doWork(String task) {
		sleep 1000
		task.reverse()
	}
}
