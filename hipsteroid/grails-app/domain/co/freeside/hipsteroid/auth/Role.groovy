package co.freeside.hipsteroid.auth

class Role {

	public static final String USER = 'ROLE_USER'

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
