class window.TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.templates.timeline
  pageTitle: 'Timeline'

  initialize: (options) =>
    @model = options.model
    @listenTo @model, 'add', @_onPictureAdded
    @listenTo @model, 'reset', @render

  render: =>
    @$el.html @template(@model.toJSON())
    @attach()

    @

  attach: =>
    @pictureList = @$el.find('ul')

    @model.each (picture) =>
      view = new PictureView
        model: picture
        el: @$el.find("li[data-id=#{picture.id}]")
      view.attach()

    @

  _onPictureAdded: (picture, collection, options) =>
    view = new PictureView
      model: picture

    @pictureList.find('li').eq(options.index).before view.render().el

  removeAll: =>
    @pictureList.children().remove()