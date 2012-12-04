window.hipsteroid = window.hipsteroid || {}

class window.HipsteroidApp extends Backbone.Router

  routes:
    timeline: 'timeline'
    upload: 'upload'

  initialize: (options) ->
    _.bindAll @

    Handlebars.registerPartial 'picture', Handlebars.templates.picture

    @appEl = $ '#app'
    @currentView = null

    @nav = new NavView
      router: @

    @pictures = new PictureCollection

    @eventBus = new vertx.EventBus('http://localhost:8585/eventbus') # todo: don't hardcode
    @eventBus.onopen = =>
      console.log 'event bus available...'

  start: (options) ->
    @preRendered = options?.preRendered ? false

    if options?.models
      @pictures.reset options.models,
        silent: true

    hasRoute = Backbone.history.start
      pushState: true
      root: hipsteroid.urlMappings.root

    unless hasRoute
      @navigate '/timeline', trigger: true

  timeline: ->
    @pictures.fetch() unless @preRendered
    @_load new TimelineView
      model: @pictures
      el: if @preRendered then @appEl.children() else undefined

  upload: ->
    @_load new UploadPictureView
      app: @

  _load: (view) ->
    @currentView?.remove()
    @currentView = view

    if @preRendered
      @currentView.attach()
      @preRendered = false
    else
      @appEl.html @currentView.render().el
