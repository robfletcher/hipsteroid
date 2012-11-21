package co.freeside.hipsteroid.auth

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
import twitter4j.*

class AuthService {

	static transactional = false

	boolean isAuthenticated() {
		try {
			twitter.id
			true
		} catch (IllegalStateException e) {
			false
		}
	}

	long getCurrentUserId() {
		twitter.id
	}

	String getCurrentUserName() {
		twitter.screenName
	}

	User getCurrentUser() {
		twitter.verifyCredentials()
	}

	private static Twitter getTwitter() {
		GrailsWebRequest requestContext = RequestContextHolder.currentRequestAttributes()
		requestContext.session.twitter ?: new TwitterFactory().instance
	}

}
