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

    @router = options.router
    @eventBus = new vertx.EventBus('http://localhost:8585/eventbus') # todo: don't hardcode
    @address = 'hipsteroid.filter.thumb.callback' # todo: generate
    @eventBus.onopen = @_registerThumbnailReciever
    @model = new Picture

  render: ->
    @$el.html @template(hipsteroid.urlMappings)

    @thumbContainer = @$el.find('.thumb-container')
    @progressBar = @$el.find('progress').hide()

    @$el.find(':file').fileupload
      dataType: 'json'
      start: @_onStart
      progressall: @_onProgress
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
    progress = parseInt(data.loaded / data.total * 100, 10)
    @progressBar.attr('value', progress).text("#{progress}%")

  _registerThumbnailReciever: ->
    @eventBus.registerHandler @address, @_onThumbnailRecieved

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
    @model.save [],
      success: @_onUploadSuccess
      error: @_onUploadFailed
    false

  _onUploadSuccess: ->
    @router.navigate '/timeline', trigger: true

  _onUploadFailed: (model, xhr, options) ->
    console.error 'upload failed', xhr.status