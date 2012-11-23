<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Hipsteroid</title>

		<r:require modules="hipsteroid"/>

		<style>
		figure img {
			width: 200px;
		}

		figcaption img {
			width: 32px;
		}
		</style>
	</head>

	<body>
		<header>
			<h1>Hipsteroid</h1>

			<div class="auth">
				<sec:ifLoggedIn>
					Logged in as <sec:loggedInUserInfo field="username"/>
					<g:form controller="logout" action="index">
						<button type="submit">Sign out</button>
					</g:form>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<g:link controller="login" action="auth">Sign in</g:link>
				</sec:ifNotLoggedIn>
			</div>
		</header>

		<div id="app"></div>

		<script>
			URLMappings = {
				pictures: '${createLink(controller: 'picture')}'
			}
		</script>
		<script id="picture-template" type="text/x-handlebars-template">
			<figure>
				<img src="{{url}}">
				<figcaption>
					<span class="meta">Uploaded {{friendlyTime dateCreated}} by {{uploadedBy.screenName}}</span>
					<button type="button" class="delete">Delete</button>
				</figcaption>
			</figure>
		</script>

	</body>
</html>
