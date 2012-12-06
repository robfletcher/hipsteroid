class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.templates.timeline
  pageTitle: 'Timeline'

  initialize: (options) ->
    _.bindAll @

    @model = options.model
    @model.on 'add', @_onPictureAdded
    @model.on 'reset', @render

  render: ->
    @$el.html @template(@model.toJSON())
    @attach()

    @

  attach: ->
    @pictureList = @$el.find('ul')

    @model.each (picture) =>
      view = new PictureView
        model: picture
        el: @$el.find("li[data-id=#{picture.id}]")
      view.attach()

    @

  remove: ->
    @model.off 'add', @_onPictureAdded
    @model.off 'reset', @render
    Backbone.View.prototype.remove.call @

  _onPictureAdded: (picture, collection, options) ->
    view = new PictureView
      model: picture

    @pictureList.find('li').eq(options.index).before view.render().el

  removeAll: ->
    @pictureList.children().remove()