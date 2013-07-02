package com.energizedwork.hipsteroid.filter

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import com.rabbitmq.client.*
import groovy.transform.CompileStatic

@CompileStatic
class Receiver {

	private static final String QUEUE_NAME = "hello"

	private final MessageHandler handler

	private final Channel channel
	private final Connection connection
	private boolean stopped = false

	private final AtomicInteger counter = new AtomicInteger()
	private final CountDownLatch stopLatch = new CountDownLatch(1)

	Receiver(MessageHandler handler) {
		def factory = new ConnectionFactory()
		factory.host = "localhost"
		connection = factory.newConnection()
		channel = connection.createChannel()

		this.handler = handler
	}

	void start() {
		channel.queueDeclare QUEUE_NAME, false, false, false, null
		println " [*] Waiting for messages."

		def consumer = new QueueingConsumer(channel)
		channel.basicConsume QUEUE_NAME, false, consumer

		while (!stopped && channel.isOpen()) {
			def delivery = consumer.nextDelivery(50)

			if (delivery) {
				counter.incrementAndGet()

				def props = delivery.properties
				def replyProps = new AMQP.BasicProperties.Builder().correlationId(props.correlationId).build()

				def reply = handler.onMessage(delivery.body)

				println " [x] Done"
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
