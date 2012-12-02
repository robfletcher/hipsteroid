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

    @eventBus = new vertx.EventBus('http://localhost:8585/eventbus') # todo: don't hardcode
    @eventBus.onopen = =>
      console.log 'event bus available...'

  start: ->
    hasRoute = Backbone.history.start
      pushState: true
      root: hipsteroid.urlMappings.root

    @navigate '/timeline', trigger: true unless hasRoute

  timeline: ->
    @pictures.fetch()
    @_load new TimelineView
      model: @pictures

  upload: ->
    @_load new UploadPictureView
      app: @

  _load: (view) ->
    @currentView?.remove()

    @appEl.html view.render().el
    @currentView = view

jQuery ->
  window.hipsteroid.app = new HipsteroidApp
  window.hipsteroid.app.start()