package co.freeside.hipsteroid

class CorsFilters {

	def filters = {
		all(uri: '/**') {
			before = {
				response.setHeader 'Access-Control-Allow-Origin', '*'
			}
		}
	}
}
