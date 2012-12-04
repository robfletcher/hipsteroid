package co.freeside.hipsteroid

import co.freeside.hipsteroid.auth.User
import grails.converters.JSON
import org.bson.types.ObjectId
import org.vertx.groovy.core.Vertx

class Picture {

	ObjectId id
	Date dateCreated
	Date lastUpdated
	User uploadedBy

	static hasOne = [imageData: ImageData]

	static constraints = {
		image bindable: true
		dateCreated bindable: false
		lastUpdated bindable: false
	}

	static mapping = {
		sort dateCreated: 'desc'
		uploadedBy updateable: false
	}

	static transients = ['image']

	void setImage(byte[] image) {
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

    Vertx vertx

    void afterInsert() {
        vertx.eventBus.publish('hipsteroid.timeline.new-pictures', JSON.parse(this.encodeAsJSON()))
    }

}
