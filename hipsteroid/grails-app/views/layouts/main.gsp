<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="description" content="">
		<meta name="viewport" content="width=device-width">

		<r:use modules="modernizr, normalize, hipsteroid"/>
		<g:layoutHead/>
		<r:layoutResources/>

		<%-- TODO: get rid of this --%>
		<style>
		figure img {
			width: 300px;
		}

		figcaption img {
			width: 32px;
		}
		</style>
	</head>

	<body>
		<header>
			<h1><g:layoutTitle default="Hipsteroid"/></h1>

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

		<div id="app">
			<g:layoutBody/>
		</div>

		<r:script>
			window.hipsteroid = window.hipsteroid || {};
			window.hipsteroid.uuid = '${UUID.randomUUID()}';
			window.hipsteroid.urlMappings = window.hipsteroid.urlMappings || {};
			window.hipsteroid.urlMappings.root = '${createLink(uri: '/')}';
			window.hipsteroid.urlMappings.pictures = '${createLink(controller: 'picture')}';
			window.hipsteroid.urlMappings.thumbnail = '${createLink(controller: 'thumbnail', action: 'generate')}';
			<sec:ifLoggedIn>
				window.hipsteroid.currentUser = new window.User({
					id: '${sec.loggedInUserInfo(field: 'id')}',
				username: '${sec.loggedInUserInfo(field: 'username')}'
			});
			</sec:ifLoggedIn>
		</r:script>

		<!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
		<r:script>
			var _gaq = [
				['_setAccount', 'UA-XXXXX-X'],
				['_trackPageview']
			];
			(function(d, t) {
				var g = d.createElement(t), s = d.getElementsByTagName(t)[0];
				g.src = ('https:' == location.protocol ? '//ssl' : '//www') + '.google-analytics.com/ga.js';
				s.parentNode.insertBefore(g, s)
			}(document, 'script'));
		</r:script>
		<r:layoutResources/>
	</body>
</html>
