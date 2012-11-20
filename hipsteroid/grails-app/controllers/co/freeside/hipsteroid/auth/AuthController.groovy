package co.freeside.hipsteroid.auth

import twitter4j.*
import twitter4j.auth.*

class AuthController {

    def login() {
		def twitter = new TwitterFactory().instance
		session.twitter = twitter

		def callbackURL = g.createLink(action: 'callback', absolute: true)
		println "Callback URL: $callbackURL"

		def requestToken = twitter.getOAuthRequestToken(callbackURL)
		session.requestToken = requestToken

		redirect url: requestToken.authenticationURL
	}

	def logout() {
		session.invalidate()

		redirect uri: '/'
	}

	def callback() {
		Twitter twitter = session.twitter
		RequestToken requestToken = session.requestToken
		def verifier = params['oauth_verifier']
		twitter.getOAuthAccessToken(requestToken, verifier)
		session.removeAttribute('requestToken')

		redirect uri: '/'
	}

}
