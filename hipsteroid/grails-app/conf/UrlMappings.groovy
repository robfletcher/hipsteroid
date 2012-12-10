class UrlMappings {

	static mappings = {

		"/oauth/$provider/success" controller: 'springSecurityOAuth', action: 'onSuccess'
		"/oauth/$provider/failure" controller: 'springSecurityOAuth', action: 'onFailure'

		'/sign-in' controller: 'login', action: 'auth'
		'/denied' controller: 'login', action: 'denied'
		'/link-account'(controller: 'springSecurityOAuth') {
			action = [GET: 'askToLinkOrCreateAccount', POST: 'linkAccount']
		}
		'/create-account'(controller: 'springSecurityOAuth', action: 'createAccount')

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

        // URIs that pre-render the one-page app in a particular state
		'/timeline' controller: 'landing', action: 'timeline'
		'/upload' controller: 'landing', action: 'upload'

		'500' view: '/error'

	}
}
