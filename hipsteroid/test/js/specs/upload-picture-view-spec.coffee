describe 'UploadPictureView', ->

  beforeEach ->
    # stub out externalities
    window.urlMappings = {}
    $.fn.fileupload = (options) ->

    @view = new UploadPictureView().render()

  describe 'uploading images', ->

    beforeEach ->
      @view._onStart()
      @progressBar = @view.$el.find('progress')

    it 'creates a progress bar when the upload starts', ->

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

    it 'replaces the progress bar with the generated thumbnails when the upload completes', ->
      @view._onComplete null, result: [
          {thumbnail: 'http://localhost/thumbnail/1'}
          {thumbnail: 'http://localhost/thumbnail/2'}
          {thumbnail: 'http://localhost/thumbnail/3'}
        ]

      expect(@view.$el.find('progress')).not.toExist()

      thumbnails = @view.$el.find('.thumb-container img')
      expect(thumbnails.length).toBe 3
      expect(thumbnails.eq(i).attr('src')).toBe "http://localhost/thumbnail/#{i + 1}" for i in [0..2]