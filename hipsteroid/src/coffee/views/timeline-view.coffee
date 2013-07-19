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

    $pictureListItems = @pictureList.find('li')
    newListItem = view.render().el
    if $pictureListItems.length is 0
      @pictureList.append newListItem
    else
      index = collection.indexOf picture
      if index is 0
        @pictureList.prepend newListItem
      else
        $pictureListItems.eq(index - 1).after newListItem

  removeAll: =>
    @pictureList.children().remove()