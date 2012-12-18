modules = {

	modernizr {
		resource url: 'lib/modernizr/modernizr-2.6.2.min.js', exclude: 'minify', disposition: 'head'
	}

	normalize {
		resource url: 'lib/normalize/normalize-1.0.1.css'
	}

	underscore {
		resource url: 'lib/underscore/underscore-1.4.2.min.js', exclude: 'minify'
	}

	backbone {
		dependsOn 'underscore'

		resource url: 'lib/backbone/backbone-0.9.9.min.js', exclude: 'minify'
	}

	handlebars {
		resource url: 'lib/handlebars/handlebars-1.0.rc.1.min.js', exclude: 'minify'
	}

	moment {
		resource url: 'lib/moment/moment-1.7.2.min.js', exclude: 'minify'
	}

	sockjs {
		resource url: 'lib/sockjs/sockjs-0.3.4.min.js', exclude: 'minify'
	}

	vertx {
		dependsOn 'sockjs'

		resource url: 'lib/vertx/vertxbus-1.3.0.final.js'
	}

	lettering {
		dependsOn 'jquery'

		resource url: 'lib/lettering/jquery.lettering-0.6.1.min.js'
	}

	'jquery-file-upload' {
		dependsOn 'jquery'

		resource url: 'lib/jquery-file-upload/js/vendor/jquery.ui.widget.js'
		resource url: 'lib/jquery-file-upload/js/jquery.iframe-transport.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload.js'
		resource url: 'lib/jquery-file-upload/js/jquery.fileupload-fp.js'
	}

	hipsteroid {
		dependsOn 'jquery', 'backbone', 'handlebars', 'moment', 'vertx', 'jquery-file-upload', 'lettering'

		resource url: 'app/view-helpers.js'
		resource url: 'app/templates.js'

		resource url: 'app/models/user.js'
		resource url: 'app/models/picture.js'
		resource url: 'app/collections/picture-collection.js'
		resource url: 'app/views/nav-view.js'
		resource url: 'app/views/picture-view.js'
		resource url: 'app/views/timeline-view.js'
		resource url: 'app/views/upload-picture-view.js'

		resource url: 'app/logo.js'

		resource url: 'app/hipsteroid.js'
		resource url: 'app/hipsteroid.css'
	}

}