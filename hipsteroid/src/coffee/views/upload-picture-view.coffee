class window.UploadPictureView extends Backbone.View

  tagName: 'section'
  className: 'upload'
  template: Handlebars.templates['upload-form']
  pageTitle: 'Upload a picture'

  events:
    'submit form': '_onSubmit'
    'change input[name=image]': '_onImageSelected'
    'change input[name=filter]': '_onFilterSelected'

  initialize: (options) =>
    @app = options.app
    @thumbCallbackAddress = "hipsteroid.filter.thumb.callback.#{hipsteroid.uuid}"
    @uploadCallbackAddress = "hipsteroid.filter.upload.callback.#{hipsteroid.uuid}"

    if @app.eventBus.readyState() is vertx.EventBus.OPEN
      @_registerThumbReciever()
    else
      @app.eventBus.onopen = @_registerThumbReciever

    @model = new Picture

  render: =>
    @$el.html @template(hipsteroid.urlMappings)
    @attach()
    @

  attach: =>
    @thumbContainer = @$el.find('.thumb-container')

    @maskInput = $ '<input type="text" readonly class="mask" placeholder="Choose an image file&hellip;" tabindex="-1">'
    @$el.find('input[name=image]').addClass('masked').parent().prepend(@maskInput)

    @$el.find(':file').fileupload
      dataType: 'json'
#      start: @_onThumbStart
#      progressall: @_onThumbProgress
      replaceFileInput: false
      formData:
        address: @thumbCallbackAddress

    @$el.find(':radio:first').attr('checked', true).trigger('change') unless @$el.find(':radio:checked').length

    @

  remove: =>
    @app.eventBus.unregisterHandler @thumbCallbackAddress, @_onThumbRecieved
    @app.eventBus.unregisterHandler @uploadCallbackAddress, @_uploadCallback
    Backbone.View.prototype.remove.call @

  _registerThumbReciever: =>
    @app.eventBus.registerHandler @thumbCallbackAddress, @_onThumbRecieved

  _onThumbRecieved: (message) =>
    @thumbContainer.find(".#{message.filter} img").attr('src', message.thumbnail)

  _onImageSelected: (event) =>
    file = event.target.files[0]

    @maskInput.val(file.name)

    reader = new FileReader
    reader.onload = (loadEvent) =>
      @model.set image: loadEvent.target.result
    reader.readAsDataURL(file)

  _onFilterSelected: (event) =>
    @model.set filter: $(event.target).val()

    @thumbContainer.find('label').removeClass('checked').find(':checked').parent().addClass('checked')

  _onSubmit: =>
    @app.eventBus.registerHandler @uploadCallbackAddress, @_uploadCallback

    @model.on 'request', @_onUploadStart

    @model.save
      callbackAddress: @uploadCallbackAddress
    ,
      error: @_onUploadFailed
    false

  _uploadCallback: =>
    @app.navigate '/timeline', trigger: true

  _onUploadStart: =>
    @$el.find(':submit').prop('disabled', true)

  _onUploadFailed: (model, response) =>
    errorList = $('<ul></ul>')
    errorList.append("<li>#{error}</li>") for error in JSON.parse(response.responseText).errors
    if (@$el.find('.errors').length)
      @$el.find('.errors').html(errorList)
    else
      errorList.insertAfter(@$el.find('h1')).wrap('<div class="errors"/>')
