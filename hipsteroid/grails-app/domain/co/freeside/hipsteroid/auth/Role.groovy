package co.freeside.hipsteroid.auth

import org.bson.types.ObjectId

class Role {

	public static final String USER = 'ROLE_USER'

	ObjectId id
	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
