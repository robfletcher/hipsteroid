<section class="timeline">
    <handlebars:render template="upload-form"/> <%-- picks up model from page scope --%>
</section>
<r:script>
	$(function() {
		window.hipsteroid.app = new HipsteroidApp;
		window.hipsteroid.app.start({preRendered: true});
	});
</r:script>