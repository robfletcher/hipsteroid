import java.awt.*
import co.freeside.hipsteroid.Filter
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.*
import org.vertx.groovy.core.file.*
import org.vertx.java.core.AsyncResult

EventBus eventBus = vertx.eventBus
FileSystem fileSystem = vertx.fileSystem

def handler = { Dimension resizeTo, Filter filter, Message message ->

	println "got message for $filter.name filter..."

	def buffer = new Buffer(message.body)

	def filename = "${UUID.randomUUID()}.jpg"

	println "about to write to $filename..."
	fileSystem.writeFile(filename, buffer) { AsyncResult<AsyncFile> tempFile ->
		println "about to execute imagemagick..."

		// This is crap and synchronous
		def fileObj = new File(filename)
		filter.execute(fileObj, fileObj, resizeTo)

		println "sending back result of $filter.name filter..."
		message.reply(new Buffer(fileObj.bytes))
		fileObj.delete()
	}

}

def sizes = [
		thumb: new Dimension(100, 100),
		full: new Dimension(640, 640)
]

sizes.each { size ->
	Filter.ALL.each { filter ->
		def address = "hipsteroid.filter.${filter.name}.${size.key}"
		eventBus.registerHandler(address, handler.curry(size.value, filter)) {
			println "$address listening..."
		}
	}
}
