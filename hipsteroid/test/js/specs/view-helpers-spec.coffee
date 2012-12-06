describe 'view helpers', ->

  describe 'friendlyTime', ->

    beforeEach ->
      @template = Handlebars.compile('{{friendlyTime t}}')
      @timestamp = moment().subtract('minutes', 10).format()

    it 'should format a JSON timestamp string', ->
      input = {t: @timestamp}
      output = $(@template(input))
      expect(output.is('time')).toBeTruthy
      expect(output.attr('datetime')).toBe @timestamp
      expect(output.text()).toBe '10 minutes ago'

  describe 'isCurrentUser', ->

    beforeEach ->
      window.hipsteroid =
        currentUser:
          id: '1'
          username: 'roundhouse'

      @template = Handlebars.compile('{{#isCurrentUser user}}BODY{{/isCurrentUser}}')

    afterEach ->
      window.hipsterord = undefined

    it 'should render the block body if the context user is the current user', ->
      user =
        id: '1'
        username: 'roundhouse'

      output = @template {user: user}
      expect(output).toBe 'BODY'

    it 'should not render the block body if the context user is not the current user', ->
      user =
        id: '2'
        username: 'ponytail'

      output = @template {user: user}
      expect(output).toBe ''
