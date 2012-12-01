class window.UploadPictureView extends Backbone.View

  tagName: 'section'
  className: 'upload'
  template: Handlebars.templates['upload-form']

  events:
    'submit form': '_onSubmit'
    'change input[name=image]': '_onImageSelected'
    'change input[name=filter]': '_onFilterSelected'

  initialize: (options) ->
    _.bindAll @

    @eventBus = new vertx.EventBus('http://localhost:8585/eventbus') # todo: don't hardcode
    @address = 'hipsteroid.filter.thumb.callback' # todo: generate
    @eventBus.onopen = @_registerThumbnailReciever
    @model = new Picture

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

  remove: ->
    @eventBus.unregisterHandler @address, @_onThumbnailRecieved
    Backbone.View.prototype.remove.call @

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

  _onImageSelected: (event) ->
    file = event.target.files[0]
    console.log 'image selected...', file
    reader = new FileReader
    reader.onload = (loadEvent) =>
      console.log 'onload', loadEvent
      @model.set image: loadEvent.target.result
    reader.readAsDataURL(file)

  _onFilterSelected: (event) ->
    console.log 'filter selected', event
    @model.set filter: $(event.target).val()

  _onSubmit: ->
    console.log 'submitting...'
    @model.save [],
      success: @_onUploadSuccess
      error: @_onUploadFailed
    false

  _onUploadSuccess: ->
    console.log 'upload succeeded', arguments
    window.app.navigate '/timeline', trigger: true

  _onUploadFailed: (model, xhr, options) ->
    console.log 'upload failed', xhr.status