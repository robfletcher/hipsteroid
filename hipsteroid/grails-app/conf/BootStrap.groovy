import java.text.SimpleDateFormat
import javax.servlet.ServletContext
import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import com.github.jknack.handlebars.*
import com.github.jknack.handlebars.io.ServletContextTemplateLoader
import grails.converters.JSON
import grails.util.Environment
import humanize.Humanize
import org.bson.types.ObjectId
import org.vertx.groovy.core.http.HttpServer
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*
import static humanize.Humanize.naturalTime

class BootStrap {

	def grailsApplication
	def grailsLinkGenerator
	def fixtureLoader
	def vertx
	def handlebarsService

	def init = { servletContext ->

		fixHandlebarsTemplateLoader servletContext
		registerHandlebarsHelpers()

		registerJsonHandlers()

		if (Environment.current in [DEVELOPMENT, TEST]) {
			ensureDefaultUsersAndRolesExist()
			loadDefaultTestData()
		}

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
		def templateLoader = new ServletContextTemplateLoader(servletContext, templatesRoot, '.handlebars')
		handlebarsService.handlebars = new Handlebars(templateLoader)
	}

	private void registerHandlebarsHelpers() {
		handlebarsService.handlebars.registerHelper('friendlyTime', new Helper<String>() {
			@Override
			CharSequence apply(String context, Options options) throws IOException {
				def date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(context)
				new Handlebars.SafeString("<time datetime=\"${context}\">${naturalTime(date)}</time>")
			}
		})
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
		def outboundPermitted = [[address_re: /hipsteroid\.filter\..+/]]

		vertx.createSockJSServer(httpServer).bridge(prefix: '/eventbus', inboundPermitted, outboundPermitted)

		def port = config.vertx.eventBus.bridge.port
		httpServer.listen(port)
		println "vertx listening on port $port"
	}

	private void ensureDefaultUsersAndRolesExist() {
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
