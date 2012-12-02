baseUrl = 'http://localhost:8080/hipsteroid'
username = 'hipsteroid'
filters = ['gotham', 'toaster', 'nashville', 'lomo', 'kelvin']

casper.waitForThumbnail = (name, selector) ->
  @waitFor ->
    @getElementAttribute(selector, 'src').match '^data:image/jpeg;base64,'
  , ->
    @test.pass "#{name} thumbnail appeared"
  , ->
    @test.fail "#{name} thumbnail did not appear"

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'data nuked!'

casper.thenOpen "#{baseUrl}/timeline", ->
  @test.info 'when a user logs in...'
  @click 'a.login'

casper.then ->
  @fill 'form',
    j_username: username
    j_password: 'hipsteroid'
  , true

casper.then ->
  @test.assertEquals @fetchText('.logged-in-message'), "Logged in as #{username}", 'user is now logged in'
  @click 'nav a[href="upload"]'

casper.then ->
  @test.info 'thumbnails are automatically generated when an image is selected'
  @fill 'form#upload-image',
    image: 'test/resources/manhattan_raw.jpg'
    filter: 'nashville'
  , false
  @waitForThumbnail(filter, ".thumb-container .#{filter} img") for filter in filters

casper.then ->
  @test.assertEquals @getElementAttribute('progress', 'value'), '100', 'image has been read'

  @test.info 'when the form is submitted'
  @click('form#upload-image [type=submit]')

casper.then ->
  @waitFor ->
    @fetchText('#app h2') == 'Timeline'
  , ->
    @test.assertUrlMatch /\/timeline$/, 'user is returned to the timeline page'
    @test.assertExists '.timeline li', 'the new image appears in the timeline'
    @test.assertDoesntExist '.timeline li:nth-child(2)', 'only one image appears in the timeline'
  , ->
    @test.fail "user should have been returned to the timeline page but heading is #{@fetchText('#app h2')}"

casper.run ->
  @test.done()