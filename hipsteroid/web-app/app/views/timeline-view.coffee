class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.templates.timeline

  initialize: (options) ->
    _.bindAll @

    @preRendered = options.el?

    @model = options.model
    @model.on 'add', @addOne
    @model.on 'reset', @render

  render: ->
    Handlebars.partials = Handlebars.templates # TODO: evil hack!!!
    @$el.html @template(@model.toJSON()) unless @preRendered
    @pictureList = @$el.find('ul')
    @addAll()

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
    @model.each (picture) =>
      new PictureView
        model: picture
        el: @$el.find("li[data-id=#{picture.id}]")

  removeAll: ->
    @pictureList.children().remove()