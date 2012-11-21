package co.freeside.hipsteroid.auth

import twitter4j.*

class AuthTagLib {

	static namespace = 'auth'

	def currentUser = { attrs ->
		User user = session.user

		if (user) {
			out << '<p>Logged in as <a href="https://twitter.com/' << user.screenName << '">' << user.name << '</a> <img src="' << user.profileImageURL << '">' << '</p>'
		}
	}

	def button = { attrs ->
		Twitter twitter = session.twitter

		if (twitter) {
			out << g.form(controller: 'auth', action: 'signOut') {
				out << '<button type="submit">Sign out</button>'
			}
		} else {
			out << g.form(controller: 'auth', action: 'signIn') {
				out << '<button type="submit">Sign in with Twitter</button>'
			}
		}
	}

}
