<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Create or Link Account</title>
	</head>

	<body>

		<g:if test="${flash.error}">
			<div class="errors">${flash.error}</div>
		</g:if>

		<p class="info"><i class="icon-warning-sign"></i> <g:message code="springSecurity.oauth.registration.link.not.exists" default="No user was found with this account." args="[session.springSecurityOAuthToken.providerName]"/></p>

		<section class="create-account">
			<g:hasErrors bean="${createAccountCommand}">
				<div class="errors">
					<g:renderErrors bean="${createAccountCommand}" as="list"/>
				</div>
			</g:hasErrors>

			<g:form action="createAccount" method="post" autocomplete="off">
				<fieldset>
					<legend><g:message code="springSecurity.oauth.registration.create.legend" default="Create a new account"/></legend>
					<label class="${hasErrors(bean: createAccountCommand, field: "username", "error")}">
						<span class="label-text"><g:message code="OAuthCreateAccountCommand.username.label" default="Username"/></span>
						<g:textField name="username" value="${createAccountCommand?.username}" placeholder="${message(code: 'OAuthCreateAccountCommand.username.label', default: 'Username')}"/>
					</label>
					<label class="${hasErrors(bean: createAccountCommand, field: "password1", "error")}">
						<span class="label-text"><g:message code="OAuthCreateAccountCommand.password1.label" default="Password"/></span>
						<g:passwordField name="password1" value="${createAccountCommand?.password1}" placeholder="${message(code: 'OAuthCreateAccountCommand.password1.label', default: 'Password')}"/>
					</label>
					<label class="${hasErrors(bean: createAccountCommand, field: "password2", "error")}">
						<span class="label-text"><g:message code="OAuthCreateAccountCommand.password2.label" default="Confirm password"/></span>
						<g:passwordField name="password2" value="${createAccountCommand?.password2}" placeholder="${message(code: 'OAuthCreateAccountCommand.password2.label', default: 'Confirm password')}"/>
					</label>
					<fieldset class="buttons">
						<button type="submit"><g:message code="springSecurity.oauth.registration.create.button" default="Create"/></button>
					</fieldset>
				</fieldset>
			</g:form>
		</section>

		<section class="link-account">
			<g:hasErrors bean="${linkAccountCommand}">
				<div class="errors">
					<g:renderErrors bean="${linkAccountCommand}" as="list"/>
				</div>
			</g:hasErrors>

			<g:form action="linkAccount" method="post" autocomplete="off">
				<fieldset>
					<legend><g:message code="springSecurity.oauth.registration.login.legend" default="Link to an existing account"/></legend>
					<label class="${hasErrors(bean: linkAccountCommand, field: "username", "error")}">
						<span class="label-text"><g:message code="OAuthLinkAccountCommand.username.label" default="Username"/></span>
						<g:textField name="username" value="${linkAccountCommand?.username}" placeholder="${message(code: 'OAuthLinkAccountCommand.username.label', default: 'Username')}"/>
					</label>
					<label class="${hasErrors(bean: linkAccountCommand, field: "password", "error")}">
						<span class="label-text"><g:message code="OAuthLinkAccountCommand.password.label" default="Password"/></span>
						<g:passwordField name="password" value="${linkAccountCommand?.password}" placeholder="${message(code: 'OAuthLinkAccountCommand.password.label', default: 'Password')}"/>
					</label>
					<fieldset class="buttons">
						<button type="submit"><g:message code="springSecurity.oauth.registration.login.button" default="Sign in"/></button>
					</fieldset>
				</fieldset>
			</g:form>
		</section>

	</body>
</html>