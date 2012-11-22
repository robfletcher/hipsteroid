import co.freeside.hipsteroid.*

class BootStrap {

	def init = { servletContext ->

		if (Picture.count() == 0) {

			['gibson', 'manhattan', 'martini', 'oldfashioned'].each {
				def image = new File(BootStrap.getResource("/${it}.jpg").toURI())
				new Picture(image: image.bytes, uploadedBy: 61233112).save(failOnError: true)
			}

		}

	}

	def destroy = {
	}
}
