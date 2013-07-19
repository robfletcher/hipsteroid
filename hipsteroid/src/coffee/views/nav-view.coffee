class window.NavView extends Backbone.View

  el: $ 'header nav'

  initialize: (options) =>
    @router = options.router

  events:
    'click a': '_handleClick'

  _handleClick: (event) =>
    @router.navigate $(event.target).attr('href'), trigger: true
    false
