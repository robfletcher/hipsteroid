import org.vertx.groovy.core.Vertx

beans = {

	def vertxClusterHost = grailsApplication.config.vertx.cluster.host
	def vertxClusterPort = grailsApplication.config.vertx.cluster.port
	println "$vertxClusterHost:$vertxClusterPort"
	vertx(Vertx, vertxClusterPort, vertxClusterHost) { bean ->
		bean.factoryMethod = 'newVertx'
	}

}
