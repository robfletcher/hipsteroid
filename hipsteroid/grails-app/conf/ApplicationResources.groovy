modules = {

	modernizr {
		resource url: 'lib/modernizr/modernizr-2.6.2.min.js', exclude: 'minify', disposition: 'head'
	}

	normalize {
		resource url: 'lib/normalize/normalize-1.0.1.css'
	}

	underscore {
		resource url: 'lib/underscore/underscore-1.5.1.min.js', exclude: 'minify'
	}

	backbone {
		dependsOn 'underscore'

		resource url: 'lib/backbone/backbone-1.0.0.min.js', exclude: 'minify'
	}

	handlebars {
		resource url: 'lib/handlebars/handlebars-1.0.0.js'
	}

	moment {
		resource url: 'lib/moment/moment-1.7.2.min.js', exclude: 'minify'
	}

	sockjs {
		resource url: 'lib/sockjs/sockjs-0.3.4.min.js', exclude: 'minify'
	}

	vertx {
		dependsOn 'sockjs'

		resource url: 'lib/vertx/vertxbus-1.3.1.final.js'
	}

	lettering {
		dependsOn 'jquery'

		resource url: 'lib/lettering/jquery.lettering-0.6.1.min.js'
	}

	'font-awesome' {
		resource url: 'lib/font-awesome/css/font-awesome.min.css', exclude: 'minify'
	}

	'jquery-file-upload' {
		dependsOn 'jquery'

		resource url: 'lib/jquery-file-upload/js/vendor/jquery.ui.widget.js'
		resource url: 'lib/jquery-file-upload/js/jquery.iframe-transport.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload-fp.js'
	}

	hipsteroid {
		dependsOn 'jquery', 'backbone', 'handlebars', 'moment', 'vertx', 'jquery-file-upload', 'lettering', 'font-awesome'

		resource url: 'scripts/view-helpers.js'
		resource url: 'scripts/templates.js'

		resource url: 'scripts/models/user.js'
		resource url: 'scripts/models/picture.js'
		resource url: 'scripts/collections/picture-collection.js'
		resource url: 'scripts/views/nav-view.js'
		resource url: 'scripts/views/picture-view.js'
		resource url: 'scripts/views/timeline-view.js'
		resource url: 'scripts/views/upload-picture-view.js'

		resource url: 'scripts/logo.js'

		resource url: 'scripts/hipsteroid.js'
		resource url: 'styles/hipsteroid.css'
	}

}