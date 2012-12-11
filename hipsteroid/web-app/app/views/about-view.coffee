class window.AboutView extends Backbone.View

  tagName: 'section'
  className: 'about'
  template: Handlebars.templates.about
  pageTitle: 'About'

  initialize: (options) ->
    _.bindAll @

  render: ->
    @$el.html @template()
    @attach()

    @

  attach: ->
    @$el.find('a').lettering()

    @
