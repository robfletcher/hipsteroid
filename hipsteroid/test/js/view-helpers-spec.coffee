describe 'view helpers', =>

  describe 'friendlyTime', =>

    beforeEach =>
      @template = Handlebars.compile('{{friendlyTime t}}')
      @timestamp = moment().subtract('minutes', 10).format()

    it 'should format a JSON timestamp string', =>
      input = {t: @timestamp}
      output = $(@template(input))
      expect(output.is('time')).toBeTruthy
      expect(output.attr('datetime')).toBe @timestamp
      expect(output.text()).toBe '10 minutes ago'