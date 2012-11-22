package co.freeside.hipsteroid

import java.util.zip.CRC32
import org.bson.types.ObjectId

class Picture {

	ObjectId id
	Date dateCreated
	Date lastUpdated
	Long uploadedBy
	String checksum
	transient byte[] image

	static constraints = {
		image bindable: true
		dateCreated bindable: false
		lastUpdated bindable: false
		checksum bindable: false
	}

	static mapping = {
		uploadedBy updateable: false
	}

	void setImage(byte[] bytes) {
		image = bytes
		def crc = new CRC32()
		crc.update(bytes)
		checksum = Long.toHexString(crc.value)
	}

	transient File getFile() {
		dateCreated ? new File(imageRoot(), "${dateCreated.format('yyyy/MM/dd')}/${id}.jpg") : null
	}

	void afterInsert() {
		file.parentFile.mkdirs()
		file.bytes = image
	}

	void afterUpdate() {
		file.bytes = image
	}

	void afterDelete() {
		file.delete()
	}

	def grailsApplication

	private File imageRoot() {
		grailsApplication.config.hipsteroid.image.root
	}

}
