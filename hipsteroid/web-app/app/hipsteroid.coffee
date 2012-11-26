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
  template: Handlebars.compile $('script#picture-template').html()
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

class UploadPictureView extends Backbone.View
  tagName: 'section'
  className: 'upload'
  template: Handlebars.compile $('script#upload-form-template').html()

  initialize: (options) ->
    _.bindAll @

  render: ->
    @$el.html @template()

    @progressBar = @$el.find('progress')

    @$el.find(':file').fileupload
      dataType: 'json'
      progressall: @_onProgress
      done: @_onComplete

    @

  _onProgress: (event, data) ->
    console.log '_onProgress', event, data
    progress = parseInt(data.loaded / data.total * 100, 10)
    @progressBar.attr('value', progress)

  _onComplete: (event, data) ->
    console.log '_onComplete', event, data

class TimelineView extends Backbone.View

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

class NavView extends Backbone.View
  el: $ 'header nav'

  initialize: (options) ->
    _.bindAll @
    @router = options.router

    @$el.find('a').click @_handleClick

  _handleClick: (event) ->
    @router.navigate $(event.target).attr('href'), trigger: true
    false

class HipsteroidApp extends Backbone.Router
  routes:
    timeline: 'timeline'
    upload: 'upload'

  initialize: (options) ->
    _.bindAll @

    @appEl = $ '#app'
    @currentView = null

    @nav = new NavView
      router: @

    @pictures = new PictureCollection
    @pictures.fetch()

  timeline: ->
    @_load new TimelineView
      model: @pictures

  upload: ->
    @_load new UploadPictureView

  _load: (view) ->
    @currentView?.remove()

    @appEl.html view.render().el
    @currentView = view

jQuery ->
  window.app = new HipsteroidApp

  hasRoute = Backbone.history.start
    pushState: true
    root: window.root

  window.app.navigate 'timeline', trigger: true unless hasRoute