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

	<% twitter4j.Twitter twitter = session.twitter ?: new twitter4j.TwitterFactory().instance %>

	<body>
		<header>
			<h1>Hipsteroid</h1>

			<div class="auth">
				<auth:currentUser/>
				<auth:button/>
			</div>
		</header>

		<div id="app"></div>

		<script>
			URLMappings = {
				pictures: '${createLink(mapping: 'pictures')}'
			}
		</script>
		<script id="picture-template" type="text/x-handlebars-template">
			<figure>
				<img src="{{url}}">
				<figcaption>
					<dl>
						<dt>Uploaded by</dt>
						{{#with uploadedBy}}
						<dd>{{screenName}}<img src="{{profileImageURL}}"></dd>
						{{/with}}
						<dt>Uploaded at</dt>
						<dd>{{dateCreated}}</dd>
						<dt>Updated at</dt>
						<dd>{{lastUpdated}}</dd>
					</dl>
				</figcaption>
			</figure>
		</script>

	</body>
</html>
