baseUrl = 'http://localhost:8080/hipsteroid'
username = 'hipsteroid'

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'data nuked!'

casper.thenOpen "#{baseUrl}/fixture/pictures/cocktails", ->
  @test.assertHttpStatus 201, 'fixture data loaded'

casper.thenOpen "#{baseUrl}/timeline", ->
  @test.info 'when no user is logged in...'
  @test.assertDoesntExist '.timeline button.delete', 'there are no delete buttons on images in the timeline'

  @test.info 'when a user logs in...'
  @click 'a.login'

casper.then ->
  @fill 'form',
    j_username: username
    j_password: 'hipsteroid'
  , true

casper.then ->
  @test.assertEquals @fetchText('.logged-in-message'), "Logged in as #{username}", 'user is now logged in'
  @test.assertExists '.timeline button.delete', 'there are delete buttons on images the user uploaded'

  @test.info 'when the user clicks a delete button'
  @click '.timeline li:first-child button.delete'
  @test.assertDoesntExist '.timeline li:nth-child(4)', 'an image is deleted'

casper.thenOpen "#{baseUrl}/timeline", ->
  @test.info 'when the page is reloaded...'
  @test.assertExists '.timeline li:nth-child(3)', 'there are 3 images'
  @test.assertDoesntExist '.timeline li:nth-child(4)', 'there are not 4 images'

  @click 'button.logout'

casper.run ->
  @test.done()