class window.UploadPictureView extends Backbone.View
  tagName: 'section'
  className: 'upload'
  template: Handlebars.templates['upload-form']

  initialize: (options) ->
    _.bindAll @

    @eventBus = new vertx.EventBus('http://localhost:8585/eventbus') # todo: don't hardcode
    @address = 'hipsteroid.filter.thumb.callback' # todo: generate
    @eventBus.onopen = @_registerThumbnailReciever

  render: ->
    @$el.html @template(urlMappings)

    @thumbContainer = @$el.find('.thumb-container')
    @progressBar = @$el.find('progress').hide()

    @$el.find(':file').fileupload
      dataType: 'json'
      start: @_onStart
      progressall: @_onProgress
      done: @_onComplete
      replaceFileInput: false
      formData:
        address: @address

    @

  _onStart: (event, data) ->
    @progressBar.show()

  _onProgress: (event, data) ->
    console.log '_onProgress', event, data
    progress = parseInt(data.loaded / data.total * 100, 10)
    @progressBar.attr('value', progress).text("#{progress}%")

  _onComplete: (event, data) ->
    console.log '_onComplete', event, data

  _registerThumbnailReciever: ->
    console.log "registering handler on #{@address}"
    @eventBus.registerHandler @address, @_onThumbnailRecieved

  _onThumbnailRecieved: (message) ->
    console.log 'received a thumbnail', message.filter
    @progressBar.hide()
    @thumbContainer.find(".#{message.filter} img").attr('src', message.thumbnail)