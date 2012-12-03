describe 'picture collections', ->

  beforeEach ->
    @collection = new PictureCollection

  describe 'ordering', ->

    beforeEach ->
      @collection.add
        id: '50bbff30ef86dfbc37d52d63'
        dateCreated: '2012-12-03T00:57:12Z'

      @collection.add
        id: '50bc00dbef86dfbc37d52d79'
        dateCreated: '2012-12-03T01:30:52Z'

      @collection.add
        id: '50bbff30ef86dfbc37d52d65'
        dateCreated: '2012-12-03T01:01:19Z'

    it 'is ordered newest first', ->
      dates = @collection.pluck('dateCreated')
      expect(dates[0]).toBe '2012-12-03T01:30:52Z'
      expect(dates[1]).toBe '2012-12-03T01:01:19Z'
      expect(dates[2]).toBe '2012-12-03T00:57:12Z'
