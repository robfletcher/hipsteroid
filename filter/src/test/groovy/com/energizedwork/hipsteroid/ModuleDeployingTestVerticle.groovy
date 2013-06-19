package com.energizedwork.hipsteroid

import org.vertx.java.core.AsyncResult
import org.vertx.java.core.AsyncResultHandler
import org.vertx.testtools.TestVerticle

abstract class ModuleDeployingTestVerticle extends TestVerticle {

	@Override
	void start() {
		// Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
		initialize()
		// Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
		// don't have to hardcode it in your tests
		container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
			@Override
			void handle(AsyncResult<String> asyncResult) {
				// Deployment is asynchronous and this this handler will be called when it's complete (or failed)
				if (asyncResult.failed()) {
					container.logger().error asyncResult.cause()
				}
				assert asyncResult.succeeded()
				assert asyncResult.result(), "deploymentID should not be null"
				// If deployed correctly then start the tests!
				startTests()
			}
		});
	}

}
