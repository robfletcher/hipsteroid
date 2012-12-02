import co.freeside.hipsteroid.auth.SimpleAjaxAwareAuthenticationEntryPoint
import org.vertx.groovy.core.Vertx
import static org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.getSecurityConfig

beans = {

	def vertxClusterHost = grailsApplication.config.vertx.cluster.host
	def vertxClusterPort = grailsApplication.config.vertx.cluster.port
	println "$vertxClusterHost:$vertxClusterPort"
	vertx(Vertx, vertxClusterPort, vertxClusterHost) { bean ->
		bean.factoryMethod = 'newVertx'
	}

	authenticationEntryPoint(SimpleAjaxAwareAuthenticationEntryPoint) {
		loginFormUrl = securityConfig.auth.loginFormUrl
		forceHttps = securityConfig.auth.forceHttps
		useForward = securityConfig.auth.useForward
		portMapper = ref('portMapper')
		portResolver = ref('portResolver')
	}
}
