class UrlMappings {

	static mappings = {

		"/oauth/$provider/success" controller: 'springSecurityOAuth', action: 'onSuccess'
		"/oauth/$provider/failure" controller: 'springSecurityOAuth', action: 'onFailure'

		// REST endpoints

		"/pictures"(controller: 'picture') {
			action = [GET: 'list', POST: 'save']
		}
		"/pictures/$id"(controller: 'picture') {
			action = [GET: 'show', PUT: 'update', DELETE: 'delete']
		}

		'/thumbnail' controller: 'thumbnail', action: 'generate'

		// testing only

		"/fixture/nuke" controller: 'fixture', action: 'nuke'
		"/fixture/$name**" controller: 'fixture', action: 'load'

		"/$controller/$action?/$id?" {
			constraints {
			}
		}

		'/' view: '/index'
		'/timeline' controller: 'landing', action: 'timeline'
		'/upload' view: '/index'
		'500' view: '/error'

	}
}
