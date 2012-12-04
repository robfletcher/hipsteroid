class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.templates.timeline

  initialize: (options) ->
    _.bindAll @

    @model = options.model
    @model.on 'add', @addOne
    @model.on 'reset', @render

  render: ->
    @$el.html @template(@model.toJSON())
    @pictureList = @$el.find('ul')
    @attach()

    @

  attach: ->
    @model.each (picture) =>
      view = new PictureView
        model: picture
        el: @$el.find("li[data-id=#{picture.id}]")
      view.attach()

    @

  remove: ->
    @model.off 'add', @addOne
    @model.off 'reset', @render
    Backbone.View.prototype.remove.call @

  addOne: (picture) ->
    view = new PictureView
      model: picture

    @pictureList.append view.render().el # todo: insert at correct position

  addAll: ->
    @model.each @addOne

  removeAll: ->
    @pictureList.children().remove()