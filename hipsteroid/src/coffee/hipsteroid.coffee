window.hipsteroid = window.hipsteroid || {}

class window.HipsteroidApp extends Backbone.Router

  routes:
    timeline: 'timeline'
    upload: 'upload'

  initialize: (options) =>
    $.ajaxSetup
      statusCode:
        401: ->
          window.location.replace('/sign-in')
        403: ->
          window.location.replace('/denied')

    Handlebars.registerPartial 'picture', Handlebars.templates.picture

    @appEl = $ '#app'
    @currentView = null

    @nav = new NavView
      router: @

    @pictures = new PictureCollection

    @eventBus = new vertx.EventBus("#{window.hipsteroid.eventBus.host}:#{window.hipsteroid.eventBus.port}/eventbus")
    @eventBus.onopen = =>
      console.log 'event bus available...'

      @eventBus.registerHandler 'hipsteroid.pictures.created', (picture) =>
        @pictures.add picture

      @eventBus.registerHandler 'hipsteroid.pictures.deleted', (picture) =>
        @pictures.remove picture.id

  start: (options) =>
    @preRendered = options?.preRendered ? false

    if options?.models
      @pictures.reset options.models,
        silent: true

    hasRoute = Backbone.history.start
      pushState: true
      root: hipsteroid.urlMappings.root

    unless hasRoute
      @navigate '/timeline', trigger: true

  timeline: =>
    @pictures.fetch() unless @preRendered
    @_load new TimelineView
      model: @pictures
      el: if @preRendered then @appEl.children() else undefined

  upload: =>
    @_load new UploadPictureView
      app: @
      el: if @preRendered then @appEl.children() else undefined

  _load: (view) =>
    @currentView?.remove()
    @currentView = view

    if @preRendered
      @currentView.attach()
      @preRendered = false
    else
      @appEl.html @currentView.render().el

    document.title = "Hipsteroid: #{@currentView.pageTitle}" if @currentView.pageTitle?
