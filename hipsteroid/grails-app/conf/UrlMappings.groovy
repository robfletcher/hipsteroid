class UrlMappings {

	static mappings = {

		'/signIn' controller: 'auth', action = 'signIn'
		'/signOut' controller: 'auth', action: 'signOut'
		'/oauth/callback' controller: 'auth', action: 'callback'

		"/pictures"(controller: 'picture') {
			action = [GET: 'list', POST: 'save']
		}
		"/pictures/$id"(controller: 'picture') {
			action = [GET: 'show', PUT: 'update', DELETE: 'delete']
		}

		"/fixture/$name**"(controller: 'fixture', action: 'load')

		"/$controller/$action?/$id?" {
			constraints {
			}
		}

		'/' view: '/index'
		'500' view: '/error'
	}
}
