package co.freeside.hipsteroid

import org.bson.types.ObjectId

class Picture {

	ObjectId id
	Date dateCreated
	Long uploadedBy
	transient byte[] image
	transient File file

	static constraints = {
		image bindable: true
		file nullable: true
	}

	def grailsApplication

	void onLoad() {
		file = new File(dir(), "${id}.jpg")
		println "Reading >> $file.absolutePath"
	}

	void afterInsert() {
		file = new File(dir(), "${id}.jpg")
		file.parentFile.mkdirs()
		println "Writing << $file.absolutePath"
		file << image
	}

	private File dir() {
		new File(imageRoot(), dateCreated.format('yyyy/MM/dd'))
	}

	private File imageRoot() {
		grailsApplication.config.hipsteroid.image.root
	}

}
