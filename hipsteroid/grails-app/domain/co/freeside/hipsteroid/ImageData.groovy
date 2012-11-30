package co.freeside.hipsteroid

import java.util.zip.*
import org.apache.commons.io.IOUtils
import org.apache.commons.io.output.NullOutputStream
import org.bson.types.ObjectId

class ImageData {

	ObjectId id
	byte[] data
	static belongsTo = [picture: Picture]

	static constraints = {
	}

	long checksumCRC32() {
		def checksum = new CRC32()
		def stream = new CheckedInputStream(new ByteArrayInputStream(data), checksum)
		IOUtils.copy(stream, new NullOutputStream())
		checksum.value
	}

}
