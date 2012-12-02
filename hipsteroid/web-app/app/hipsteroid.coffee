window.hipsteroid = window.hipsteroid || {}

class window.HipsteroidApp extends Backbone.Router
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

  timeline: ->
    @pictures.fetch()
    @_load new TimelineView
      model: @pictures

  upload: ->
    @_load new UploadPictureView
      router: @

  _load: (view) ->
    @currentView?.remove()

    @appEl.html view.render().el
    @currentView = view

jQuery ->
  window.hipsteroid.app = new HipsteroidApp

  hasRoute = Backbone.history.start
    pushState: true
    root: window.hipsteroid.urlMappings.root

  window.hipsteroid.app.navigate '/timeline', trigger: true unless hasRoute