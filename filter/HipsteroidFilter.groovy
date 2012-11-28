import co.freeside.hipsteroid.Filter
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.*
import org.vertx.groovy.core.file.*
import org.vertx.java.core.AsyncResult

EventBus eventBus = vertx.eventBus
FileSystem fileSystem = vertx.fileSystem

def handler = { Filter filter, Message message ->

	println "got message for $filter.name filter..."

	def buffer = new Buffer(message.body)

	def filename = "${UUID.randomUUID()}.jpg"

	println "about to write to $filename..."
	fileSystem.writeFile(filename, buffer) { AsyncResult<AsyncFile> tempFile ->
		println "about to execute imagemagick..."

		// This is crap and synchronous
		def fileObj = new File(filename)
		filter.execute(fileObj, fileObj)

		println "sending back result of $filter.name filter..."
		message.reply(new Buffer(fileObj.bytes))
		fileObj.delete()
	}

}

Filter.ALL.each { filter ->
	eventBus.registerHandler("hipsteroid.filter.$filter.name", handler.curry(filter)) {
		println "$filter.name listening..."
	}
}
