package com.energizedwork.hipsteroid.filter

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import groovy.transform.CompileStatic
import groovy.util.logging.Log

@CompileStatic
@Log
class Main {

	static void main(String... args) {
		Collection<Receiver> receivers = []

		for (size in ImageSize.values()) {
			for (filter in Filter.ALL) {
				def handler = new FilterHandler(filter, size.dimension)
				def queueName = "hipsteroid.filter.${filter.name}.${size.name()}"
				receivers << new Receiver(queueName, handler)
			}
		}

		def executor = Executors.newFixedThreadPool(receivers.size())
		for (receiver in receivers) {
			executor.submit {
				log.info "Starting $receiver.queueName..."
				receiver.start()
			}
		}

		addShutdownHook {
			for (receiver in receivers) {
				log.info "Stopping $receiver.queueName..."
				receiver.close()
				log.info "Stopped $receiver.queueName"
			}
			log.info "Stopping work queue..."
			executor.awaitTermination(3, TimeUnit.SECONDS)
			log.info "kthxbye"
		}
	}
}
