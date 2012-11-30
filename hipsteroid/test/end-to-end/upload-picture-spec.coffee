baseUrl = 'http://localhost:8080/hipsteroid'

casper.waitForThumbnail = (selector) ->
  @waitFor ->
    @getElementAttribute(selector, 'src') == 'data:image/jpeg;base64,'
  , ->
    @test.pass 'thumbnail appeared'
  , ->
    @test.fail 'thumbnail did not appear'

filters = ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']

casper.on 'remote.message', (msg) ->
  @test.info msg
casper.on 'page.error', (message, trace) ->
  @test.fail "Error: #{message}"

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'Data nuked!'

casper.thenOpen "#{baseUrl}/upload", ->
  @test.info 'thumbnails are automatically generated when an image is selected'
  @test.assertExists 'form#upload-image'
  @test.assertExists 'input[type=file]'
  @fill 'form#upload-image',
    image: 'test/resources/manhattan_raw.jpg'
  , false
  @evaluate ->
    $('input[type=file]').trigger('change')
  @test.assertExists ".thumb-container .#{filter} img" for filter in filters
  @waitForThumbnail(".thumb-container .#{filter} img") for filter in ['gotham']

casper.then ->
  @test.assertEquals @getElementAttribute('progress', 'value'), '100'

casper.run ->
  @test.done()