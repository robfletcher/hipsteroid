modules = {

	modernizr {
		resource url: 'lib/modernizr/modernizr.min.js', exclude: 'minify', disposition: 'head'
	}

	normalize {
		resource url: 'lib/normalize/normalize.css'
	}

	underscore {
		resource url: 'lib/underscore/underscore-min.js'
	}

	backbone {
		dependsOn 'underscore'

		resource url: 'lib/backbone/backbone-min.js'
	}

	handlebars {
		resource url: 'lib/handlebars/handlebars-1.0.rc.1.min.js'
	}

	hipsteroid {
		dependsOn 'jquery', 'backbone', 'handlebars'
		resource url: 'app/view-helpers.js'
		resource url: 'app/hipsteroid.js'
	}

}