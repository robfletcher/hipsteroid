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

    @app = options.app
    @thumbCallbackAddress = "hipsteroid.filter.thumb.callback.#{hipsteroid.uuid}"
    @uploadCallbackAddress = "hipsteroid.filter.upload.callback.#{hipsteroid.uuid}"

    if @app.eventBus.readyState() is vertx.EventBus.OPEN
      @_registerThumbnailReciever()
    else
      @app.eventBus.onopen = @_registerThumbnailReciever

    @model = new Picture

  render: ->
    @$el.html @template(hipsteroid.urlMappings)
    @attach()
    @

  attach: ->
    @thumbContainer = @$el.find('.thumb-container')
    @progressBar = @$el.find('progress').hide()

    @$el.find(':file').fileupload
      dataType: 'json'
      start: @_onStart
      progressall: @_onProgress
      replaceFileInput: false
      formData:
        address: @thumbCallbackAddress

    @

  remove: ->
    @app.eventBus.unregisterHandler @thumbCallbackAddress, @_onThumbnailRecieved
    @app.eventBus.unregisterHandler @uploadCallbackAddress, @_uploadCallback
    Backbone.View.prototype.remove.call @

  _registerThumbnailReciever: ->
    @app.eventBus.registerHandler @thumbCallbackAddress, @_onThumbnailRecieved

  _onStart: (event, data) ->
    @progressBar.show()

  _onProgress: (event, data) ->
    progress = parseInt(data.loaded / data.total * 100, 10)
    @progressBar.attr('value', progress).text("#{progress}%")

  _onThumbnailRecieved: (message) ->
    @progressBar.hide()
    @thumbContainer.find(".#{message.filter} img").attr('src', message.thumbnail)

  _onImageSelected: (event) ->
    file = event.target.files[0]
    reader = new FileReader
    reader.onload = (loadEvent) =>
      @model.set image: loadEvent.target.result
    reader.readAsDataURL(file)

  _onFilterSelected: (event) ->
    @model.set filter: $(event.target).val()

  _onSubmit: ->
    @app.eventBus.registerHandler @uploadCallbackAddress, @_uploadCallback
    @model.save
      callbackAddress: @uploadCallbackAddress
    ,
      error: @_onUploadFailed
    false

  _uploadCallback: ->
    @app.navigate '/timeline', trigger: true

  _onUploadFailed: (model, xhr, options) ->
    console.error 'upload failed', xhr.status