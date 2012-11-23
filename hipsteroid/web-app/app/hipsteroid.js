var hipsteroid = {};

hipsteroid.Picture = Backbone.Model.extend({});

hipsteroid.PictureCollection = Backbone.Collection.extend({

	model: hipsteroid.Picture,
	url: URLMappings.pictures,

	comparator: function(a, b) {
		if (a.dateCreated > b.dateCreated) return -1;
		else if (a.dateCreated < b.dateCreated) return 1;
		return 0;
	}
});

hipsteroid.PictureTimeline = Backbone.View.extend({

	tagName: 'section',
	className: 'timeline',
	template: Handlebars.compile($('script#picture-template').html()),

	initialize: function(options) {
		_.bindAll(this);
		this.model = options.model;
		this.model.on('all', this.render);
	},

	render: function() {
		this.model.each(this.append);
	},

	append: function(model) {
		this.$el.append(this.template(model.toJSON()));
	}

});

hipsteroid.AppView = Backbone.View.extend({

	el: $('#app'),

	initialize: function() {

		this.pictures = new hipsteroid.PictureCollection;
		this.timeline = new hipsteroid.PictureTimeline({model: this.pictures});
		this.$el.append(this.timeline.el);
		this.pictures.fetch();

	}

});

$(function() {

	window.Hipsteroid = new hipsteroid.AppView();

});