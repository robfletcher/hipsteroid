package com.energizedwork.hipsteroid.filter

import java.awt.*
import groovy.transform.*

@CompileStatic
@TupleConstructor
class FilterHandler implements MessageHandler {

	final Filter filter
	final Dimension size

	@Override
	byte[] onMessage(byte[] message) {
		def file = File.createTempFile("${UUID.randomUUID()}", ".jpg")
		file.withOutputStream { OutputStream stream ->
			stream << message
		}
		filter.execute file, file, size
		println "ok filtered"
		return file.bytes
	}

}
