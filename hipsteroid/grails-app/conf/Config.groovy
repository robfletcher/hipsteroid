import co.freeside.hipsteroid.auth.Role

grails.config.locations = ["classpath:oauth-config.properties"]

grails.project.groupId = 'co.freeside'
grails.mime.file.extensions = true
grails.mime.use.accept.header = true
grails.mime.types = [
		all: '*/*',
		atom: 'application/atom+xml',
		css: 'text/css',
		csv: 'text/csv',
		form: 'application/x-www-form-urlencoded',
		html: ['text/html', 'application/xhtml+xml'],
		jpg: 'image/jpeg',
		js: 'text/javascript',
		json: ['application/json', 'text/json'],
		multipartForm: 'multipart/form-data',
		rss: 'application/rss+xml',
		text: 'text/plain',
		xml: ['text/xml', 'application/xml']
]

grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.views.gsp.sitemesh.preprocess = true
grails.scaffolding.templates.domainSuffix = 'Instance'

grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart = false

grails.exceptionresolver.params.exclude = ['password']

grails.hibernate.cache.queries = false

environments {
	development {
		grails.app.context = '/'
		grails.logging.jul.usebridge = true
		grails.resources.debug = true
		grails.serverURL = "http://localhost:8080"
		vertx.eventBus.bridge.port = 8085
		vertx.eventBus.bridge.host = 'http://localhost'
	}
	production {
		grails.logging.jul.usebridge = false
		grails.serverURL = 'http://hipsteroid.cloudfoundry.com'
		vertx.eventBus.bridge.port = System.getenv('PORT_EVENTBUS')?.toInteger() ?: 8085
		vertx.eventBus.bridge.host = grails.serverURL
	}
}

log4j = {
    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

rabbitmq {
	connectionFactory {
		username = 'guest'
		password = 'guest'
		hostname = 'localhost'
	}
	queues = {
		exchange(name: 'hipsteroid.filter', type: direct) {
			'none.full' binding: 'none.full'
			'gotham.full' binding: 'gotham.full'
			'toaster.full' binding: 'toaster.full'
			'nashville.full' binding: 'nashville.full'
			'lomo.full' binding: 'lomo.full'
			'kelvin.full' binding: 'kelvin.full'
			'none.thumb' binding: 'none.thumb'
			'gotham.thumb' binding: 'gotham.thumb'
			'toaster.thumb' binding: 'toaster.thumb'
			'nashville.thumb' binding: 'nashville.thumb'
			'lomo.thumb' binding: 'lomo.thumb'
			'kelvin.thumb' binding: 'kelvin.thumb'
		}
	}
}

oauth {
	providers {
		twitter {
			api = org.scribe.builder.api.TwitterApi
			successUri = '/oauth/twitter/success'
			failureUri = '/'
			callback = "${grails.serverURL}/oauth/twitter/callback"
		}
	}
}

grails.plugins.springsecurity.userLookup.userDomainClassName = 'co.freeside.hipsteroid.auth.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'co.freeside.hipsteroid.auth.UserRole'
grails.plugins.springsecurity.authority.className = 'co.freeside.hipsteroid.auth.Role'
grails.plugins.springsecurity.oauth.domainClass = 'co.freeside.hipsteroid.auth.OAuthID'
grails.plugins.springsecurity.oauth.registration.roleNames = [Role.USER]

grails.resources.mappers.handlebars.templatesRoot = 'app/templates'

grails.plugin.cloudfoundry.showStackTrace = true