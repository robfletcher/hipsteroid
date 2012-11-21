package co.freeside.hipsteroid.auth

import twitter4j.*
import twitter4j.auth.RequestToken
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST

/**
 * A very simple Twitter based authentication controller.
 */
class AuthController {

	static allowedMethods = [signIn: 'POST', signOut: 'POST', callback: 'GET']

    def signIn() {

		def twitter = new TwitterFactory().instance
		session.twitter = twitter

		def callbackURL = createLink(action: 'callback', absolute: true)
		def requestToken = twitter.getOAuthRequestToken(callbackURL)
		session.requestToken = requestToken

		redirect url: requestToken.authenticationURL

	}

	def signOut() {

		session.invalidate()

		redirect uri: '/'

	}

	def callback() {

		if (!params.oauth_verifier) {
			render status: SC_BAD_REQUEST, text: 'Missing parameter "oauth_verifier"'
		}

		Twitter twitter = session.twitter
		RequestToken requestToken = session.requestToken
		twitter.getOAuthAccessToken(requestToken, params.oauth_verifier)
		session.removeAttribute('requestToken')

		session.user = twitter.verifyCredentials()

		redirect uri: '/'

	}

}
