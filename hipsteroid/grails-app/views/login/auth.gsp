<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title><g:message code="springSecurity.login.title"/></title>
	</head>

	<body>

		<section class="sign-in">
			<h1><g:message code="springSecurity.login.header"/></h1>

			<g:if test="${flash.message}">
				<div class="errors"><i class="icon-warning-sign"></i> ${flash.message}</div>
			</g:if>

			<nav class="oauth">
				<oauth:connect provider="twitter" id="twitter-connect-link"><i class="icon-twitter"></i> Sign in with Twitter</oauth:connect>
			</nav>

			<p class="divider">or&hellip;</p>

			<form action="${postUrl}" method="POST" id="loginForm" autocomplete="off">
				<fieldset>
					<legend><i class="icon-camera-retro"></i> Sign in with your Hipsteroid account</legend>

					<label>
						<span class="label-text"><g:message code="springSecurity.login.username.label"/></span>
						<input type="text" name="j_username" placeholder="${message(code: 'springSecurity.login.username.label')}" autofocus>
					</label>

					<label>
						<span class="label-text"><g:message code="springSecurity.login.password.label"/></span>
						<input type="password" name="j_password" placeholder="${message(code: 'springSecurity.login.password.label')}">
					</label>

					<label class="checkbox">
						<input type="checkbox" name="${rememberMeParameter}" <g:if test="${hasCookie}">checked</g:if>>
						<span class="label-text"><g:message code="springSecurity.login.remember.me.label"/></span>
					</label>
				</fieldset>

				<fieldset class="buttons">
					<button type="submit"><g:message code="springSecurity.login.button"/></button>
				</fieldset>
			</form>
		</section>

	</body>
</html>
