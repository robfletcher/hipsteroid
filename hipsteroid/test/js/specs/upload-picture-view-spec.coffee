describe 'UploadPictureView', ->

  beforeEach ->
    setFixtures '<div class="upload-form"></div>'

    # stub out externalities
    window.urlMappings = {}
    $.fn.fileupload = (options) ->

    @view = new UploadPictureView
      router:
        navigate: ->
          console.log arguments
    @view.render()
    $('.upload-form').append(@view.el)

    @progressBar = @view.$el.find('progress')

  it 'hides the progress bar until an image is selected', ->
    expect(@progressBar).not.toBeVisible()

  describe 'generating thumbnails', ->

    beforeEach ->
      @view._onStart()

    it 'shows the progress bar when an image is selected', ->
      expect(@progressBar).toBeVisible()
      expect(@progressBar.attr('value')).toBe 0
      expect(@progressBar.attr('max')).toBe '100'
      expect(@progressBar.text()).toBe '0%'

    it 'updates the progress bar as the upload proceeds', ->
      @view._onProgress null,
        loaded: 256
        total: 1024

      expect(@progressBar.attr('value')).toBe 25
      expect(@progressBar.text()).toBe '25%'

      @view._onProgress null,
        loaded: 512
        total: 1024

      expect(@progressBar.attr('value')).toBe 50
      expect(@progressBar.text()).toBe '50%'

    it 'hides the progress bar when the first thumbnail comes back', ->
      message =
        filter: 'lomo'
        thumbnail: 'data:image/jpeg;base64,lomo'

      @view._onThumbnailRecieved message

      expect(@view.$el.find('progress')).not.toBeVisible()
      expect(@view.$el.find('.lomo img').attr('src')).toBe(message.thumbnail)

    it 'adds more thumbnails as they arrive', ->
      message1 =
        filter: 'lomo'
        thumbnail: 'data:image/jpeg;base64,lomo'
      message2 =
        filter: 'nashville'
        thumbnail: 'data:image/jpeg;base64,nashville'

      @view._onThumbnailRecieved message for message in [message1, message2]

      expect(@view.$el.find('.lomo img').attr('src')).toBe(message1.thumbnail)
      expect(@view.$el.find('.nashville img').attr('src')).toBe(message2.thumbnail)
