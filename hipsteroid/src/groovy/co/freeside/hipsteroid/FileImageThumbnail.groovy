package co.freeside.hipsteroid

import groovy.transform.TupleConstructor

@TupleConstructor
class FileImageThumbnail implements ImageThumbnail {

	final File file

	@Override
	String getFilename() {
		file.name
	}

	@Override
	InputStream getInputStream() {
		file.newInputStream()
	}

	@Override
	long getSize() {
		file.length()
	}
}
