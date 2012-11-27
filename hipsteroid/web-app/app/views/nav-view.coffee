class window.NavView extends Backbone.View
  el: $ 'header nav'

  initialize: (options) ->
    _.bindAll @
    @router = options.router

    @$el.find('a').click @_handleClick

  _handleClick: (event) ->
    @router.navigate $(event.target).attr('href'), trigger: true
    false
