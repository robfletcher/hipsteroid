package co.freeside.hipsteroid

import org.apache.commons.lang.StringUtils
import org.springframework.web.multipart.MultipartFile

class ImageThumbnailService {

	static transactional = false

	private final File tempDirectory = new File(System.properties.'java.io.tmpdir')

	Collection<ImageThumbnail> generatePreviews(MultipartFile original) {
		(0..3).collect {
			def id = UUID.randomUUID().toString()
			def extension = StringUtils.substringAfterLast(original.originalFilename, '.')
			def file = new File(tempDirectory, "${id}.${extension}")
			file.withOutputStream { stream ->
				stream << original.inputStream
			}
			new FileImageThumbnail(file)
		}
	}

	ImageThumbnail getThumbnail(String thumbnail) {
		def file = new File(tempDirectory, thumbnail)
		file.isFile() ? new FileImageThumbnail(file) : null
	}

}
