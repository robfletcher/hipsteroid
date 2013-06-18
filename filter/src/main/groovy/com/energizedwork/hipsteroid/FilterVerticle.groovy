package com.energizedwork.hipsteroid

import java.awt.*
import org.vertx.groovy.core.AsyncResult
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.file.AsyncFile
import org.vertx.groovy.platform.Verticle

class FilterVerticle extends Verticle {

	private static final SIZES = [
			thumb: new Dimension(100, 100),
			full: new Dimension(640, 640)
	].asImmutable()

	def start() {
		SIZES.each { size ->
			Filter.ALL.each { filter ->
				def address = "hipsteroid.filter.${filter.name}.${size.key}"
				vertx.eventBus.registerHandler(address, handler.curry(size.value, filter)) {
					container.logger.info "$address listening..."
				}
			}
		}
	}

	private Closure handler = { Dimension resizeTo, Filter filter, Message message ->

		container.logger.debug "got message for $filter.name filter with a ${message.body.getClass()}..."

		def file = new File(System.properties."java.io.tmpdir", "${UUID.randomUUID()}.jpg")

		println "about to write to $file..."
		vertx.fileSystem.writeFile(file.absolutePath, new Buffer(message.body)) { AsyncResult<AsyncFile> tempFile ->
			container.logger.debug "about to execute imagemagick..."

			// This is crap and synchronous
			filter.execute(file, file, resizeTo)

			container.logger.debug "sending back result of $filter.name filter..."
			message.reply(new Buffer(file.bytes))
			file.delete()
		}

	}
}
