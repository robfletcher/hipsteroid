package co.freeside.hipsteroid

import co.freeside.hipsteroid.auth.User
import org.bson.types.ObjectId

class Picture {

	ObjectId id
	Date dateCreated
	Date lastUpdated
	User uploadedBy

	static hasOne = [imageData: ImageData]

	static constraints = {
		image bindable: true
		imageData nullable: true // TODO: just to get async construction working
		dateCreated bindable: false
		lastUpdated bindable: false
	}

	static mapping = {
		uploadedBy updateable: false
	}

	static transients = ['image']

	void setImage(byte[] image) {
		println "setting image data with $image.length bytes"
		if (!imageData) {
			imageData = new ImageData(picture: this, data: image)
		} else {
			imageData.data = image
		}
	}

	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0]

	byte[] getImage() {
		imageData?.data ?: EMPTY_BYTE_ARRAY
	}

}
