package co.freeside.hipsteroid

import static org.apache.commons.codec.binary.Base64.*
import static org.apache.commons.lang.StringUtils.substringAfter

class DataUrlCodec {

	static encode = { byte[] target, String mimeType ->
		if (target == null) {
			return null
		}

		def buffer = new StringBuilder() << 'data:' << mimeType << ';base64,'
		buffer << new String(encodeBase64(target))
		buffer.toString()
	}

	static decode = { String target ->
		if (target == null) {
			return null
		}

		decodeBase64 substringAfter(target, ',')
	}

}
