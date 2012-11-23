grails.config.locations = ["classpath:oauth-config.properties"]

grails.project.groupId = 'co.freeside'
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
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
grails.web.disable.multipart=false

grails.exceptionresolver.params.exclude = ['password']

grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
		grails.resources.debug = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = 'http://hipsteroid.cloudfoundry.com'
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

oauth {
	providers {
		twitter {
			api = org.scribe.builder.api.TwitterApi
			successUri = '/oauth/twitter/success'
			failureUri = '/'
			callback = 'http://localhost:8080/hipsteroid/oauth/twitter/callback'
		}
	}
}

grails.plugins.springsecurity.userLookup.userDomainClassName = 'co.freeside.hipsteroid.auth.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'co.freeside.hipsteroid.auth.UserRole'
grails.plugins.springsecurity.authority.className = 'co.freeside.hipsteroid.auth.Role'
grails.plugins.springsecurity.oauth.domainClass = 'co.freeside.hipsteroid.auth.OAuthID'

hipsteroid {
	image.root = new File('target/uploads')
}
