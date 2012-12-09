import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.*
import org.vertx.groovy.core.file.FileSystem
import org.vertx.java.core.AsyncResult

def filters = ['none', 'gotham', 'toaster', 'nashville', 'lomo', 'kelvin']
EventBus eventBus = vertx.eventBus
FileSystem fileSystem = vertx.fileSystem

println 'opening file...'
fileSystem.readFile('manhattan.jpg') { AsyncResult<Buffer> input ->
	filters.each {
		def address = "hipsteroid.filter.${it}.full"
		println "requesting $address..."
		eventBus.send(address, input.result) { Message reply ->
			def outputFilename = "${it}.jpg"
			fileSystem.writeFile(outputFilename, new Buffer(reply.body)) {
				println "wrote $outputFilename"
			}
		}
	}
}