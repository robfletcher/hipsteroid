<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Hipsteroid</title>
	</head>

	<body>
		<h1>Hipsteroid</h1>

		<% twitter4j.Twitter twitter = session.twitter %>
		<% twitter4j.User user = session.user %>
		<g:if test="${twitter}">
			<g:if test="${user}">
				<p>Logged in as <a href="https://twitter.com/${user.screenName}">${user.name}</a></p>
				<img src="${user.profileImageURL}">
			</g:if>
			<g:form controller="auth" action="logout">
				<button type="submit">Log out</button>
			</g:form>
		</g:if>
		<g:else>
			<g:form controller="auth" action="login">
				<button type="submit">Log in with Twitter</button>
			</g:form>
		</g:else>
	</body>
</html>
