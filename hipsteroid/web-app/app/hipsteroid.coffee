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

class AppView extends Backbone.View

  el: $ '#app'

  initialize: ->
    _.bindAll @

    @pictures = new PictureCollection
    @pictures.on 'add', @addOne
    @pictures.on 'reset', @addAll
    @pictures.on 'all', @render

    @timeline = $('<ul class="timeline"/>')
    @$el.html(@timeline)

    @pictures.fetch()

  addOne: (picture) ->
    view = new PictureView model: picture
    @timeline.prepend view.render().el

  addAll: ->
    @pictures.each @addOne

jQuery ->
  window.app = new AppView