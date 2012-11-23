<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title><g:message code="springSecurity.login.title"/></title>
	</head>

	<body>
		<h1><g:message code="springSecurity.login.header"/></h1>

		<g:if test="${flash.message}">
			<div class="login_message">${flash.message}</div>
		</g:if>

		<oauth:connect provider="twitter" id="twitter-connect-link">Twitter</oauth:connect>

		<form action="${postUrl}" method="POST" id="loginForm" autocomplete="off">
			<p>
				<label for="username"><g:message code="springSecurity.login.username.label"/>:</label>
				<input type="text" name="j_username" id="username" autofocus>
			</p>

			<p>
				<label for="password"><g:message code="springSecurity.login.password.label"/>:</label>
				<input type="password" name="j_password" id="password">
			</p>

			<p id="remember_me_holder">
				<input type="checkbox" name="${rememberMeParameter}" id="remember_me" <g:if test="${hasCookie}">checked</g:if>>
				<label for="remember_me"><g:message code="springSecurity.login.remember.me.label"/></label>
			</p>

			<p>
				<button type="submit"><g:message code="springSecurity.login.button"/></button>
			</p>
		</form>

	</body>
</html>
