package com.energizedwork.hipsteroid

import java.util.zip.CRC32;
import org.junit.*
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.AsyncResultHandler
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.Message
import org.vertx.testtools.TestVerticle
import static org.vertx.testtools.VertxAssert.assertEquals
import static org.vertx.testtools.VertxAssert.testComplete;

/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class FilterVerticleTest extends TestVerticle {

	static final File INPUT_FILE = getResourceFile("manhattan.jpg")
	static final Map<String, File> FILTERED_FILES = [
	        gotham: getResourceFile("manhattan-gotham.jpg")
	].asImmutable()

	@Test
    public void filtersAnImage() {
		vertx.fileSystem().readFile(INPUT_FILE.absolutePath, { AsyncResult<Buffer> input ->
			vertx.eventBus().send("hipsteroid.filter.gotham.full", input.result(), { Message<Buffer> reply ->
				assertEquals "checksums do not match", getChecksum(FILTERED_FILES.gotham), getChecksum(reply.body())
				testComplete()
			} as Handler)
		} as Handler)
	}

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

	private static File getResourceFile(String name) {
		new File(FilterVerticleTest.getResource("/$name").toURI())
	}

	private static long getChecksum(Buffer buffer) {
		getChecksum buffer.bytes
	}

	private static long getChecksum(File file) {
		getChecksum file.bytes
	}

	private static long getChecksum(byte[] bytes) {
		def checksum = new CRC32()
		checksum.update(bytes)
		return checksum.value
	}

}
