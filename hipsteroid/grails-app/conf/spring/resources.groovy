import co.freeside.hipsteroid.auth.SimpleAjaxAwareAuthenticationEntryPoint
import co.freeside.hipsteroid.viewhelpers.*
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.vertx.groovy.core.Vertx
import static org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.getSecurityConfig

beans = {

	vertx(Vertx, grailsApplication.config.vertx.cluster.port, grailsApplication.config.vertx.cluster.host) { bean ->
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

	def consumerKey = application.config.twitter.consumerKey
	def consumerSecret = application.config.twitter.consumerSecret
	def accessKey = application.config.twitter.accessKey
	def accessSecret = application.config.twitter.accessSecret
	twitterTemplate(TwitterTemplate, consumerKey.toString(), consumerSecret.toString(), accessKey.toString(),  accessSecret.toString())
}
