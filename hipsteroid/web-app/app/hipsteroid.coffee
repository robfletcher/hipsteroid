window.currentUser = null
window.urlMappings =
  pictures: null
window.app = null

class window.User extends Backbone.Model

class Picture extends Backbone.Model

class PictureCollection extends Backbone.Collection

  model: Picture
  url: ->
    window.urlMappings.pictures

  comparator: (a, b) ->
    if a.dateCreated > b.dateCreated
      return -1
    else if a.dateCreated < b.dateCreated
      return 1
    return 0

class PictureView extends Backbone.View

  tagName: 'li'
  template: Handlebars.compile($('script#picture-template').html())
  events: 'click button.delete': 'delete'

  initialize: (options) ->
    _.bindAll @
    @model = options.model
    @model.on 'change', @render
    @model.on 'destroy', @remove

  render: ->
    @$el.append @template(@model.toJSON())
    @renderDeleteButton() if window.currentUser?.id is @model.get('uploadedBy').id
    @

  renderDeleteButton: ->
    @$el.find('figcaption').append('<button type="button" class="delete">Delete</button>')

  delete: ->
    @model.destroy()

class TimelineView extends Backbone.View

  tagName: 'section'
  className: 'timeline'
  template: Handlebars.compile($('script#timeline-template').html())

  initialize: (options) ->
    _.bindAll @

    @model = options.model
    @model.on 'add', @addOne
    @model.on 'reset', @addAll
    @model.on 'all', @render

  render: ->
    @$el.append @template()
    @timeline = @$el.find('ul')
    @

  addOne: (picture) ->
    view = new PictureView model: picture
    @timeline.prepend view.render().el

  addAll: ->
    @model.each @addOne

jQuery ->
  pictures = new PictureCollection

  window.app = new TimelineView
    model: pictures

  el = window.app.render().el
  $('#app').append(el)

  pictures.fetch()

