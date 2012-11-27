class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.compile $('script#timeline-template').html()

  initialize: (options) ->
    _.bindAll @

    @model = options.model
    @model.on 'add', @addOne
    @model.on 'reset', @addAll
  #    @model.on 'all', @render

  render: ->
    @$el.html @template()
    @pictureList = @$el.find('ul')
    @addAll()
    @

  addOne: (picture) ->
    view = new PictureView model: picture
    @pictureList.prepend view.render().el

  addAll: ->
    @model.each @addOne
