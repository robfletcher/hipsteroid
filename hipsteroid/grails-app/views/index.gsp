<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Hipsteroid</title>
	</head>
	<body>
		<h1>Hipsteroid</h1>
		
		<% twitter4j.Twitter twitter = session.twitter %>
		<g:if test="${twitter}">
			<p>Logged in as ${twitter.screenName}</p>
			<img src="${twitter.getProfileImage(twitter.screenName, twitter4j.ProfileImage.NORMAL).URL}">
		</g:if>
		<g:else>
			<g:link controller="auth" action="login">Log in with Twitter</g:link>
		</g:else>
	</body>
</html>
