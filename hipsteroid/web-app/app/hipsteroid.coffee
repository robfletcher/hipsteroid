window.currentUser = null
window.urlMappings =
  pictures: null
window.app = null

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
    root: window.urlMappings.root

  window.app.navigate 'timeline', trigger: true unless hasRoute