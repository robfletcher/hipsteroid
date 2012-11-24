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

hipsteroid.PictureView = Backbone.View.extend({

	tagName: 'li',
	template: Handlebars.compile($('script#picture-template').html()),

	events: {
		'click button.delete': 'delete'
	},

	initialize: function(options) {
		_.bindAll(this);
		this.model = options.model;
		this.model.on('change', this.render);
		this.model.on('destroy', this.remove);
	},

	render: function() {
		this.$el.append(this.template(this.model.toJSON()));
		return this;
	},

	delete: function() {
		this.model.destroy();
	}

});

hipsteroid.AppView = Backbone.View.extend({

	el: $('#app'),

	initialize: function() {
		_.bindAll(this);

		this.pictures = new hipsteroid.PictureCollection;
		this.pictures.on('add', this.addOne);
		this.pictures.on('reset', this.addAll);
		this.pictures.on('all', this.render);

		this.timeline = $('<ul class="timeline"/>');
		this.$el.html(this.timeline);

		this.pictures.fetch();
	},

	addOne: function(picture) {
		var view = new hipsteroid.PictureView({model: picture});
		this.timeline.prepend(view.render().el);
	},

	addAll: function() {
		this.pictures.each(this.addOne);
	}

});

$(function() {

	window.Hipsteroid = new hipsteroid.AppView();

});