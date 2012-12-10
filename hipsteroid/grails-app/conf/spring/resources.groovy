import co.freeside.hipsteroid.auth.SimpleAjaxAwareAuthenticationEntryPoint
import co.freeside.hipsteroid.viewhelpers.FriendlyTime
import co.freeside.hipsteroid.viewhelpers.IsCurrentUser

import static org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.getSecurityConfig

beans = {

	authenticationEntryPoint(SimpleAjaxAwareAuthenticationEntryPoint) {
		loginFormUrl = securityConfig.auth.loginFormUrl
		forceHttps = securityConfig.auth.forceHttps
		useForward = securityConfig.auth.useForward
		portMapper = ref('portMapper')
		portResolver = ref('portResolver')
	}

	friendlyTime(FriendlyTime)

	isCurrentUser(IsCurrentUser, ref('springSecurityService'))

}
