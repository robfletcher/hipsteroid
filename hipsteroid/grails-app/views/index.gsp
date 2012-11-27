<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>Hipsteroid</title>

		<r:require modules="hipsteroid"/>

		<style>
		figure img {
			width: 200px;
		}

		figcaption img {
			width: 32px;
		}

		.thumb-container img {
			width: 32px;
		}
		</style>
	</head>

	<body>
		<header>
			<h1>Hipsteroid</h1>

			<aside class="auth">
				<sec:ifLoggedIn>
					<span class="logged-in-message">Logged in as <sec:loggedInUserInfo field="username"/></span>
					<g:form controller="logout" action="index">
						<button type="submit" class="logout">Sign out</button>
					</g:form>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<g:link controller="login" action="auth" class="login">Sign in</g:link>
				</sec:ifNotLoggedIn>
			</aside>

			<nav>
				<a href="timeline">Timeline</a>
				<a href="upload">Upload a picture</a>
			</nav>
		</header>

		<div id="app"></div>

		<r:script>
			root = '${createLink(uri: '/')}';
			urlMappings.pictures = '${createLink(controller: 'picture')}';
			urlMappings.thumbnail = '${createLink(controller: 'thumbnail', action: 'generate')}';
			<sec:ifLoggedIn>
			currentUser = new window.User({
				id: ${sec.loggedInUserInfo(field: 'id')},
				username: '${sec.loggedInUserInfo(field: 'username')}'
			});
			</sec:ifLoggedIn>
		</r:script>

	</body>
</html>
