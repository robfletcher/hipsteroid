modules = {

	modernizr {
		resource url: 'lib/modernizr/modernizr.min.js', exclude: 'minify', disposition: 'head'
	}

	normalize {
		resource url: 'lib/normalize/normalize.css'
	}

	underscore {
		resource url: 'lib/underscore/underscore-min.js', exclude: 'minify'
	}

	backbone {
		dependsOn 'underscore'

		resource url: 'lib/backbone/backbone-min.js', exclude: 'minify'
	}

	handlebars {
		resource url: 'lib/handlebars/handlebars-1.0.rc.1.min.js', exclude: 'minify'
	}

	moment {
		resource url: 'lib/moment/moment.min.js', exclude: 'minify'
	}

	'jquery-file-upload' {
		dependsOn 'jquery'

		resource url: 'lib/jquery-file-upload/js/vendor/jquery.ui.widget.js'
		resource url: 'lib/jquery-file-upload/js/jquery.iframe-transport.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload-fp.js'
	}

	hipsteroid {
		dependsOn 'jquery', 'backbone', 'handlebars', 'moment'
		resource url: 'app/view-helpers.js'
		resource url: 'app/hipsteroid.js'
	}

}