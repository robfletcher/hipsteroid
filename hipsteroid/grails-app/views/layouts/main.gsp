<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

		<title>Hipsteroid: <g:layoutTitle/></title>

		<meta name="description" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<%-- TODO: load with JS --%>
		<link href="http://fonts.googleapis.com/css?family=Just+Another+Hand|Quattrocento+Sans:400|Maiden+Orange" rel="stylesheet" type="text/css">

		<r:use modules="modernizr, normalize, hipsteroid"/>
		<g:layoutHead/>
		<r:layoutResources/>
	</head>

	<body>
		<header>
			<h1><a href="${createLink(uri: '/')}">Hipsteroid</a></h1>

			<aside class="auth">
				<sec:ifLoggedIn>
					<span class="logged-in-message">Signed in as @<sec:loggedInUserInfo field="username"/></span>
					<g:form controller="logout" action="index">
						<button type="submit" class="logout"><i class="icon-signout"></i> Sign out</button>
					</g:form>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<oauth:connect provider="twitter" id="twitter-connect-link"><i class="icon-signin"></i> Sign in</oauth:connect>
				</sec:ifNotLoggedIn>
			</aside>

			<nav>
				<a href="/timeline"><i class="icon-time"></i> Timeline</a>
				<a href="/upload"><i class="icon-camera-retro"></i> Upload a picture</a>
			</nav>
		</header>

		<div id="app">
			<g:layoutBody/>
		</div>

		<r:script>
			window.hipsteroid = window.hipsteroid || {};
			window.hipsteroid.uuid = '${UUID.randomUUID()}';
			window.hipsteroid.eventBus = { host: '${grailsApplication.config.vertx.eventBus.bridge.host}', port: ${grailsApplication.config.vertx.eventBus.bridge.port} };
			window.hipsteroid.urlMappings = window.hipsteroid.urlMappings || {};
			window.hipsteroid.urlMappings.root = '${createLink(uri: '/')}';
			window.hipsteroid.urlMappings.pictures = '${createLink(controller: 'picture')}';
			window.hipsteroid.urlMappings.thumbnail = '${createLink(controller: 'thumbnail')}';
			<sec:ifLoggedIn>
				window.hipsteroid.currentUser = new window.User({
					id: '${sec.loggedInUserInfo(field: 'id')}',
				username: '${sec.loggedInUserInfo(field: 'username')}'
			});
			</sec:ifLoggedIn>
		</r:script>

		<r:script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
				(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
					m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', 'UA-42475343-1', 'energizedwork.com');
			ga('send', 'pageview');
		</r:script>
		<r:layoutResources/>
	</body>
</html>
