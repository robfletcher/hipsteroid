package co.freeside.hipsteroid.auth

import javax.servlet.http.*
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import static org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.isAjax

class SimpleAjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	@Override
	void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
		if (isAjax(request)) {
			response.sendError SC_UNAUTHORIZED
		} else {
			super.commence request, response, authException
		}
	}

}
