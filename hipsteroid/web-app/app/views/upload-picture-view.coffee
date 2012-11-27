class window.UploadPictureView extends Backbone.View
  tagName: 'section'
  className: 'upload'
  template: Handlebars.templates['upload-form']

  initialize: (options) ->
    _.bindAll @

  render: ->
    @$el.html @template(urlMappings)

    @thumbContainer = @$el.find('.thumb-container')

    @$el.find(':file').fileupload
      dataType: 'json'
      start: @_onStart
      progressall: @_onProgress
      done: @_onComplete

    @

  _onStart: (event, data) ->
    @progressBar = $('<progress value="0" max="100">0%</progress>')
    @thumbContainer.html @progressBar

  _onProgress: (event, data) ->
    console.log '_onProgress', event, data
    progress = parseInt(data.loaded / data.total * 100, 10)
    @progressBar.attr('value', progress).text("#{progress}%")

  _onComplete: (event, data) ->
    console.log '_onComplete', event, data
    console.log it for it in data.result
    @thumbContainer.html ''
    $('<img>', src: it.thumbnail).appendTo(@thumbContainer) for it in data.result
