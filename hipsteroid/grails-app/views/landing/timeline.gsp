<section class="timeline">
	<handlebars:render template="timeline" model="${pictures}"/>
</section>
<r:script>
	$(function() {
		window.hipsteroid.app = new HipsteroidApp;
		window.hipsteroid.app.start({preRendered: true, models: ${pictures}});
	});
</r:script>