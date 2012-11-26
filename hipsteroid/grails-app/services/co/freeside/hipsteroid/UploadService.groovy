package co.freeside.hipsteroid

import org.apache.commons.lang.StringUtils
import org.vertx.groovy.core.eventbus.*
import static javax.servlet.http.HttpServletResponse.*

class UploadService {

	static transactional = false

	def grailsApplication

	void register(EventBus eventBus) {
		eventBus.registerHandler 'upload:start', this.&start
		eventBus.registerHandler 'upload:upload', this.&upload
	}

    def start(Message message) {
		println message.body
		def uuid = UUID.randomUUID().toString()
		message.reply status: SC_CONTINUE, uuid: uuid
	}

    def upload(Message message) {
		def uuid = message.body.uuid
		println "got upload request with $uuid"
		if (uuid) {
			def file = new File(imageRoot, "${uuid}.jpg")
			file.withOutputStream { stream ->
				stream << StringUtils.substringAfter(message.body.data, ',').decodeBase64()
			}
			println "wrote some data to $file.absolutePath"
			message.reply status: SC_CONTINUE, uuid: uuid
		} else {
			message.reply status: SC_BAD_REQUEST
		}
    }

	private File getImageRoot() {
		grailsApplication.config.hipsteroid.image.root
	}

}
