baseUrl = 'http://localhost:8080/hipsteroid'

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'data nuked!'

casper.thenOpen "#{baseUrl}/fixture/pictures/cocktails", ->
  @test.assertHttpStatus 201, 'fixture data loaded'

casper.thenOpen baseUrl, ->
  @test.assertEquals @fetchText('#app h1'), 'Timeline', 'timeline page is loaded'
  @test.assertExists '.timeline li:nth-child(4)', 'data is fetched from the server'

  @test.info 'when the upload nav link is clicked'
  @click 'nav a[href="upload"]'

casper.then ->
  @test.assertEquals @fetchText('#app h1'), 'Upload a picture', 'upload page is loaded'

  @test.info 'when the timeline nav link is clicked'
  @click 'nav a[href="timeline"]'

casper.then ->
  @test.assertEquals @fetchText('#app h1'), 'Timeline', 'timeline page is loaded'

casper.run ->
  @test.done()