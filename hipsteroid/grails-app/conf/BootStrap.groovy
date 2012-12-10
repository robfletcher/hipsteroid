import java.awt.Dimension
import javax.servlet.ServletContext
import co.freeside.hipsteroid.Filter
import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.*
import com.github.jknack.handlebars.*
import com.github.jknack.handlebars.io.ServletContextTemplateLoader
import grails.converters.JSON
import grails.util.Environment
import org.bson.types.ObjectId
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.file.AsyncFile
import org.vertx.groovy.core.http.HttpServer
import org.vertx.java.core.AsyncResult
import static co.freeside.hipsteroid.auth.Role.USER
import static grails.util.Environment.*

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
//		if (Environment.current in [DEVELOPMENT, TEST]) {
			ensureDefaultUsersExist()
			loadDefaultTestData()
//		}

		startImageFilter()
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

	/**
	 * This is completely *NOT* the way to deploy a vert.x app but I don't want to have to deploy 2 things to CF.
	 */
	private void startImageFilter() {
		def handler = { Dimension resizeTo, Filter filter, Message message ->

			println "got message for $filter.name filter with a ${message.body.getClass()}..."

			def filename = "${UUID.randomUUID()}.jpg"

			println "about to write to $filename..."
			vertx.fileSystem.writeFile(filename, new Buffer(message.body)) { AsyncResult<AsyncFile> tempFile ->
				println "about to execute imagemagick..."

				// This is crap and synchronous
				def fileObj = new File(filename)
				filter.execute(fileObj, fileObj, resizeTo)

				println "sending back result of $filter.name filter..."
				message.reply(new Buffer(fileObj.bytes))
				fileObj.delete()
			}

		}

		def sizes = [
				thumb: new Dimension(100, 100),
				full: new Dimension(640, 640)
		]

		sizes.each { size ->
			Filter.ALL.each { filter ->
				def address = "hipsteroid.filter.${filter.name}.${size.key}"
				vertx.eventBus.registerHandler(address, handler.curry(size.value, filter)) {
					println "$address listening..."
				}
			}
		}
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
