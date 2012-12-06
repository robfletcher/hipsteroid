package co.freeside.hipsteroid.viewhelpers

import com.github.jknack.handlebars.*
import grails.plugins.springsecurity.SpringSecurityService
import groovy.transform.TupleConstructor
import org.codehaus.groovy.grails.web.json.JSONObject

@TupleConstructor
class IsCurrentUser implements Helper<JSONObject> {

	final SpringSecurityService springSecurityService

	@Override
	CharSequence apply(JSONObject user, Options options) throws IOException {
		if (user.id == springSecurityService.currentUser?.id?.toString()) {
			options.fn()
		}
	}

}