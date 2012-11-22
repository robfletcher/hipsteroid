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
	transient File file

	static constraints = {
		image bindable: true
		file nullable: true
		dateCreated bindable: false
		lastUpdated bindable: false
		checksum bindable: false
		file bindable: false
	}

	void setImage(byte[] bytes) {
		image = bytes
		def crc = new CRC32()
		crc.update(bytes)
		checksum = Long.toHexString(crc.value)
	}

	void onLoad() {
		file = new File(dir(), "${id}.jpg")
	}

	void afterInsert() {
		file = new File(dir(), "${id}.jpg")
		file.parentFile.mkdirs()
		file << image
	}

	void afterUpdate() {
		file.bytes = image
	}

	void afterDelete() {
		file.delete()
	}

	private File dir() {
		new File(imageRoot(), dateCreated.format('yyyy/MM/dd'))
	}

	def grailsApplication

	private File imageRoot() {
		grailsApplication.config.hipsteroid.image.root
	}

}
