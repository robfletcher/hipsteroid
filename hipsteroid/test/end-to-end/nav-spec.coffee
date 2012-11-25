baseUrl = 'http://localhost:8080/hipsteroid'

casper.start baseUrl, ->
  @test.assertEquals @fetchText('#app h2'), 'Timeline', 'timeline page is loaded'

  @test.info 'when the upload nav link is clicked'
  @click 'nav a[href="upload"]'

casper.then ->
  @test.assertEquals @fetchText('#app h2'), 'Upload a picture', 'upload page is loaded'

  @test.info 'when the timeline nav link is clicked'
  @click 'nav a[href="timeline"]'

casper.then ->
  @test.assertEquals @fetchText('#app h2'), 'Timeline', 'timeline page is loaded'

casper.run ->
  @test.done()