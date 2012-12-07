describe 'timeline view', ->

  beforeEach ->

    Handlebars.registerPartial 'picture', Handlebars.templates.picture

    window.hipsteroid =
      currentUser:
        id: '50bc9fa3300492daf6b695ae'
        username: 'hipsteroid'

    @server = sinon.fakeServer.create()

    @model = new Picture
      id: '50bdbb1530042539738e704f'
      uploadedBy:
        id: '50bc9fa3300492daf6b695ae'
        username: 'hipsteroid'
      url: 'http://localhost:8080/pictures/50bdbb1530042539738e704f'
      dateCreated: '2012-12-04T08:57:57Z'


    @collection = new PictureCollection

    # override url property to point to sinon fakeServer
    @model.url = '/pictures'
    @collection.url = '/pictures'

  afterEach ->
    @server.restore()

  describe 'attaching to a pre-rendered page', ->

    beforeEach ->
      @collection.add @model

      setFixtures """
        <div id="app">
          <section class="timeline">
            <h2>PRE-RENDERED Timeline</h2>
            <ul>
              <li data-id="#{@model.id}">
                <figure>
                  <img src="#{@model.get('url')}">
                  <figcaption>
                    <span class="meta">Uploaded <time datetime="#{@model.get('dateCreated')}">2 minutes ago</time> by #{@model.get('uploadedBy').username}</span>
                  </figcaption>
                </figure>
              </li>
            </ul>
          </section>
        </div>
                  """

      @view = new TimelineView
        model: @collection
        el: $('.timeline')

      @syncSpy = sinon.spy(Backbone, 'sync')

      @view.attach()

    afterEach ->
      Backbone.sync.restore()

    it 'should not overwrite the existing DOM elements', ->
      expect($('.timeline').find('h2').text()).toBe 'PRE-RENDERED Timeline'
      expect($('.timeline li').length).toBe 1
      expect($('.timeline img').attr('src')).toBe @model.get('url')
      expect($('.timeline .meta').text()).toBe 'Uploaded 2 minutes ago by hipsteroid'

    it 'should not sync with the server', ->
      expect(@syncSpy).not.toHaveBeenCalled()

    describe 'responding to changes in the model', ->

      beforeEach ->
        @server.respondWith 'DELETE', "/pictures/#{@model.id}", [202, {'Content-Type': 'application/json'}, "{id:'#{@model.id}'}"]

      it 'should remove elements when the corresponding model is deleted', ->
        @model.destroy()
        @server.respond()

        expect($('.timeline ul').children().length).toBe 0

  describe 'rendering to an empty page', ->

    beforeEach ->
      setFixtures '<div id="app"></div>'

    describe 'initializing the view with pre-fetched data', ->

      beforeEach ->
        @collection.add @model

      it 'renders the model contents', ->
        @view = new TimelineView
          model: @collection

        $('#app').append @view.render().el

        expect($('.timeline').find('h1').text()).toBe 'Timeline'
        expect($('.timeline li').length).toBe 1
        expect($('.timeline img').attr('src')).toBe @model.get('url')
        expect($('.timeline .meta').text()).toBe "Uploaded #{moment(@model.get('dateCreated')).fromNow()} by hipsteroid"

    describe 'fetching data once the view is already loaded', ->

      beforeEach ->
        @server.respondWith 'GET', '/pictures', [200, {'Content-Type': 'application/json'}, JSON.stringify([@model.toJSON()])]

      it 'should render the view contents when fetched', ->
        @view = new TimelineView
          model: @collection

        $('#app').append @view.render().el

        @collection.fetch()
        @server.respond()

        expect($('.timeline').find('h1').text()).toBe 'Timeline'
        expect($('.timeline li').length).toBe 1
        expect($('.timeline img').attr('src')).toBe @model.get('url')
        expect($('.timeline .meta').text()).toBe "Uploaded #{moment(@model.get('dateCreated')).fromNow()} by hipsteroid"
