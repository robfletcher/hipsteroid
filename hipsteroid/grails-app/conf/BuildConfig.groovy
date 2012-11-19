grails.servlet.version = '3.0'
grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.dependency.resolution = {

    inherits 'global'
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

    }

    plugins {
		compile ':cache-headers:1.1.5'

		runtime ":mongodb:1.0.0.GA"
        runtime ':jquery:1.8.3'
        runtime ':resources:1.2.RC2'
        runtime ':zipped-resources:1.0'
        runtime ':cached-resources:1.0'
        runtime ':yui-minify-resources:0.1.5'

        build ":tomcat:$grailsVersion"
    }
}
