class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.templates.timeline

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

  remove: ->
    @model.off 'add', @addOne
    @model.off 'reset', @addAll
    @model.off 'all', @render
    Backbone.View.prototype.remove.call @

  addOne: (picture) ->
    view = new PictureView model: picture
    @pictureList.prepend view.render().el

  addAll: ->
    @model.each @addOne
