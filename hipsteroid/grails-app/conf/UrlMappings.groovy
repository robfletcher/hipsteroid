class UrlMappings {

	static mappings = {

		'/signIn' controller: 'auth', action = 'signIn'
		'/signOut' controller: 'auth', action: 'signOut'
		'/oauth/callback' controller: 'auth', action: 'callback'

		name pictures: '/pictures'(controller: 'picture', action: 'list')
		name picture: "/picture/$id"(controller: 'picture') {
			action = [GET: 'show', PUT: 'update', DELETE: 'delete', POST: 'save']
		}

		"/$controller/$action?/$id?" {
			constraints {
			}
		}

		'/' view: '/index'
		'500' view: '/error'
	}
}
