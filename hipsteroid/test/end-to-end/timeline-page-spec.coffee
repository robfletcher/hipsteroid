casper.start 'http://localhost:8080/hipsteroid/fixture/nuke', ->
  @test.assertHttpStatus 200, 'Data nuked!'

casper.thenOpen 'http://localhost:8080/hipsteroid/fixture/pictures/cocktails', ->
  @test.assertHttpStatus 201, 'Fixture data loaded'

casper.thenOpen 'http://localhost:8080/hipsteroid', ->
  @test.assertExists '.timeline li:nth-child(4)', 'There are 4 images'
  @test.assertDoesntExist '.timeline li:nth-child(5)', 'There are not more than 4 images'

casper.thenOpen 'http://localhost:8080/hipsteroid/fixture/pictures/cocktails', ->
  @test.assertHttpStatus 201, 'Fixture data loaded'

casper.thenOpen 'http://localhost:8080/hipsteroid', ->
  @test.assertExists '.timeline li:nth-child(8)', 'There are 8 images'
  @test.assertDoesntExist '.timeline li:nth-child(9)', 'There are not more than 8 images'

casper.run ->
  @test.done()