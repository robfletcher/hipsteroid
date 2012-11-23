Handlebars.registerHelper('friendlyTime', function(timestamp) {
	var m = moment(timestamp);
	return new Handlebars.SafeString('<time datetime="' + m.format() + '">' + m.fromNow() + '</time>');
});