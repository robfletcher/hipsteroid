class UrlMappings {

	static mappings = {

		name login: '/login' {
			controller = 'auth'
			action = 'login'
		}

		name logout: '/logout' {
			controller = 'auth'
			action = 'logout'
		}

		name oauthCallback: '/oauth/callback' {
			controller = 'auth'
			action = 'callback'
		}

		"/$controller/$action?/$id?" {
			constraints {
			}
		}

		'/'(view: '/index')
		'500'(view: '/error')
	}
}
