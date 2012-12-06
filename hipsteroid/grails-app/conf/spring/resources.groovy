import co.freeside.hipsteroid.auth.SimpleAjaxAwareAuthenticationEntryPoint
import co.freeside.hipsteroid.viewhelpers.*
import org.vertx.groovy.core.Vertx
import static org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.getSecurityConfig

beans = {

	/**
	 * This is an un-clustered vert.x instance. To cluster it add port & host arguments. For local clustering the host
	 * can be set to `InetAddress.localHost.hostAddress`.
	 */
	vertx(Vertx) { bean ->
		bean.factoryMethod = 'newVertx'
	}

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
