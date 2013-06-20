grails.servlet.version = '2.5'
grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.dependency.resolution = {

	inherits('global') {
		excludes 'h2'
	}

	log 'error'
	checksums true

	repositories {

		inherits true

		mavenLocal()

		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenCentral()

	}

	dependencies {

		compile 'org.vert-x:vertx-lang-groovy:1.3.0.final',
				'org.im4java:im4java:1.2.0',
				'com.github.mfornos:humanize-slim:0.1.4'

		test "org.spockframework:spock-grails-support:0.7-groovy-2.0"

	}

	plugins {

		compile ':cache-headers:1.1.4',
				':handlebars:1.0.0',
//				':handlebars-resources:0.3.2',
				':spring-security-core:1.2.7.3',
				':spring-security-oauth:2.0.1.1'

		runtime ':mongodb:1.0.0.GA',
				':jquery:1.8.3',
				':resources:1.2.RC2',
				':zipped-resources:1.0',
				':cached-resources:1.0',
				':yui-minify-resources:0.1.5',
				':fixtures:1.2'

		build ":tomcat:$grailsVersion"

		test(":spock:0.7") {
			exclude "spock-grails-support"
		}

	}
}
