baseUrl = 'http://localhost:8080'

casper.start "#{baseUrl}/fixture/nuke", ->
  @test.assertHttpStatus 200, 'data nuked!'

casper.thenOpen "#{baseUrl}/fixture/pictures/cocktails", ->
  @test.assertHttpStatus 201, 'fixture data loaded'

casper.thenOpen "#{baseUrl}/timeline", ->
  @test.assertExists '.timeline li:nth-child(4)', 'there are 4 images'
  @test.assertDoesntExist '.timeline li:nth-child(5)', 'there are not more than 4 images'

  @test.assertEquals @fetchText('.timeline li:first-child .meta'), 'Uploaded right now by hipsteroid', 'the templates have rendered correctly'

  @test.info 'when the user navigates away from the timeline and returns'
  @click 'nav a[href="/upload"]'

casper.thenClick 'nav a[href="/timeline"]', ->
  @test.assertExists '.timeline li:nth-child(4)', 'there are 4 images'
  @test.assertDoesntExist '.timeline li:nth-child(5)', 'there are not more than 4 images'

  @test.assertEquals @fetchText('.timeline li:first-child .meta'), 'Uploaded a few seconds ago by hipsteroid', 'there is an intesting inconsistency between the server side and client side rendering'

casper.thenOpen "#{baseUrl}/fixture/pictures/cocktails", ->
  @test.assertHttpStatus 201, 'more fixture data loaded'

casper.thenOpen baseUrl, ->
  @test.assertExists '.timeline li:nth-child(8)', 'there are 8 images'
  @test.assertDoesntExist '.timeline li:nth-child(9)', 'there are not more than 8 images'

casper.run ->
  @test.done()