import javax.servlet.ServletContext
import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import com.github.jknack.handlebars.*
import com.github.jknack.handlebars.io.ServletContextTemplateLoader
import grails.converters.JSON
import org.bson.types.ObjectId
import org.vertx.groovy.core.http.HttpServer
import static co.freeside.hipsteroid.auth.Role.USER

class BootStrap {

	def grailsApplication
	def grailsLinkGenerator
	def fixtureLoader
	def vertx
	def handlebarsService
	def springSecurityService

	def init = { servletContext ->

		fixHandlebarsTemplateLoader servletContext
		registerHandlebarsHelpers()

		registerJsonHandlers()

		ensureDefaultRolesExist()
		ensureDefaultUsersExist()
		loadDefaultTestData()

		startEventBusBridge()

	}

	def destroy = {
	}

	/**
	 * Makes Handlebars partial resolution consistent with template resolution so templates can use each other as
	 * partials.
	 */
	private void fixHandlebarsTemplateLoader(ServletContext servletContext) {
		def templatesRoot = grailsApplication.config.grails.resources.mappers.handlebars.templatesRoot ?: ''
		def templateLoader = new ServletContextTemplateLoader(servletContext, templatesRoot, '.hbs')
		handlebarsService.handlebars = new Handlebars(templateLoader)
	}

	private void registerHandlebarsHelpers() {
		grailsApplication.mainContext.getBeansOfType(Helper).each { String name, Helper helper ->
			println "registering Handlebars helper: $name"
			handlebarsService.handlebars.registerHelper name, helper
		}
	}

	private void registerJsonHandlers() {
		JSON.registerObjectMarshaller(ObjectId) {
			it.toString()
		}

		JSON.registerObjectMarshaller(Picture) {
			[
					id: it.id,
					url: grailsLinkGenerator.link(controller: 'picture', action: 'show', id: it.id, absolute: true),
					uploadedBy: it.uploadedBy,
					dateCreated: it.dateCreated,
					lastUpdated: it.lastUpdated
			]
		}

		JSON.registerObjectMarshaller(User) {
			[
					id: it.id,
					username: it.username
			]
		}
	}

	private void startEventBusBridge() {
		HttpServer httpServer = vertx.createHttpServer()

		def inboundPermitted = []
		def outboundPermitted = [[address_re: /hipsteroid\..+/]]

		vertx.createSockJSServer(httpServer).bridge(prefix: '/eventbus', inboundPermitted, outboundPermitted)

		def port = config.vertx.eventBus.bridge.port
		httpServer.listen(port)
		println "vertx event bus bridge listening on port $port"
	}

	private void ensureDefaultRolesExist() {
		Role.findOrSaveByAuthority USER
	}

	private void ensureDefaultUsersExist() {
		def role = Role.findOrSaveByAuthority USER
		def user = User.findOrCreateByUsername 'hipsteroid'
		user.password = 'hipsteroid'
		user.enabled = true
		user.save failOnError: true
		UserRole.findOrSaveByUserAndRole user, role
	}

	private void loadDefaultTestData() {
		if (Picture.count() == 0) {
			fixtureLoader.load 'pictures/cocktails'
		}
	}

	private ConfigObject getConfig() {
		grailsApplication.config
	}

}
