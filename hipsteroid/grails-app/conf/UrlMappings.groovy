class UrlMappings {

	static mappings = {

		'/signIn' controller: 'auth', action = 'signIn'
		'/signOut' controller: 'auth', action: 'signOut'
		'/oauth/callback' controller: 'auth', action: 'callback'

		"/$controller/$action?/$id?" {
			constraints {
			}
		}

		'/' view: '/index'
		'500' view: '/error'
	}
}
