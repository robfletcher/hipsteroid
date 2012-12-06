class window.UploadPictureView extends Backbone.View

  tagName: 'section'
  className: 'upload'
  template: Handlebars.templates['upload-form']
  pageTitle: 'Upload a picture'

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
      @_registerThumbReciever()
    else
      @app.eventBus.onopen = @_registerThumbReciever

    @model = new Picture

  render: ->
    @$el.html @template(hipsteroid.urlMappings)
    @attach()
    @

  attach: ->
    @thumbContainer = @$el.find('.thumb-container')

    @maskInput = $ '<input type="text" readonly class="mask" placeholder="Choose an image file&hellip;" tabindex="-1">'
    @$el.find('input[name=image]').addClass('masked').parent().prepend(@maskInput)

    @maskInput = $ '<input type="text" readonly class="mask" placeholder="Choose an image file&hellip;" tabindex="-1">'
    @$el.find('input[name=image]').addClass('masked').parent().prepend(@maskInput)

    @$el.find(':file').fileupload
      dataType: 'json'
#      start: @_onThumbStart
#      progressall: @_onThumbProgress
      replaceFileInput: false
      formData:
        address: @thumbCallbackAddress

    @

  remove: ->
    @app.eventBus.unregisterHandler @thumbCallbackAddress, @_onThumbRecieved
    @app.eventBus.unregisterHandler @uploadCallbackAddress, @_uploadCallback
    Backbone.View.prototype.remove.call @

  _registerThumbReciever: ->
    @app.eventBus.registerHandler @thumbCallbackAddress, @_onThumbRecieved

#  _onThumbStart: (event, data) ->

#  _onThumbProgress: (event, data) ->
#    progress = parseInt(data.loaded / data.total * 100, 10)

  _onThumbRecieved: (message) ->
    @thumbContainer.find(".#{message.filter} img").attr('src', message.thumbnail)

  _onImageSelected: (event) ->
    file = event.target.files[0]

    @maskInput.val(file.name)

    reader = new FileReader
    reader.onload = (loadEvent) =>
      @model.set image: loadEvent.target.result
    reader.readAsDataURL(file)

  _onFilterSelected: (event) ->
    @model.set filter: $(event.target).val()

    @thumbContainer.find('label').removeClass('checked').find(':checked').parent().addClass('checked')

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