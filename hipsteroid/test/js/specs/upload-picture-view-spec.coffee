describe 'UploadPictureView', ->

  beforeEach ->
    setFixtures '<div class="upload-form"></div>'

    # stub out externalities
    window.hipsteroid =
      urlMappings: {}
    $.fn.fileupload = (options) ->

    @view = new UploadPictureView
      app:
        navigate: ->
          console.log arguments
        eventBus:
          readyState: ->
            vertx.EventBus.OPEN
          registerHandler: (address, callback) ->
            null
            
    @view.render()
    $('.upload-form').append(@view.el)

  describe 'generating thumbnails', ->

    beforeEach ->
      @view._onThumbStart()

    it 'replaces the stock image with the generated thumbnail', ->
      message =
        filter: 'lomo'
        thumbnail: 'data:image/jpeg;base64,lomo'

      @view._onThumbRecieved message

      expect(@view.$el.find('.lomo img').attr('src')).toBe(message.thumbnail)

    it 'adds more thumbnails as they arrive', ->
      message1 =
        filter: 'lomo'
        thumbnail: 'data:image/jpeg;base64,lomo'
      message2 =
        filter: 'nashville'
        thumbnail: 'data:image/jpeg;base64,nashville'

      @view._onThumbRecieved message for message in [message1, message2]

      expect(@view.$el.find('.lomo img').attr('src')).toBe(message1.thumbnail)
      expect(@view.$el.find('.nashville img').attr('src')).toBe(message2.thumbnail)
