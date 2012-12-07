baseUrl = 'http://localhost:8080'

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'data nuked!'

casper.thenOpen "#{baseUrl}/fixture/pictures/cocktails", ->
  @test.assertHttpStatus 201, 'fixture data loaded'

casper.thenOpen baseUrl, ->
  @click 'button.logout' if @exists 'button.logout'
  @test.assertEquals @fetchText('#app h1'), 'Timeline', 'timeline page is loaded'
  @test.assertExists '.timeline li:nth-child(4)', 'timeline pictures are shown'
  @test.assertDoesntExist 'button.delete', 'no delete buttons are present'

casper.thenClick 'nav a[href="upload"]', ->
  @test.info 'when an anonymous user clicks on the "Upload a picture" link in the nav'
  @test.assertTitle 'Hipsteroid: Sign in', 'they are redirected to the login page'

casper.thenOpen "#{baseUrl}/upload", ->
  @test.info 'when an anonymous user attempts to go directly to the upload page'
  @test.assertTitle 'Hipsteroid: Sign in', 'they are redirected to the login page'

casper.run ->
  @test.done()