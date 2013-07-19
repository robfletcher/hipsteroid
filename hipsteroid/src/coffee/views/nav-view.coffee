class window.NavView extends Backbone.View

  el: $ 'header nav'

  initialize: (options) =>
    @router = options.router

  events:
    'click a': '_handleClick'

  _handleClick: (event) =>
    url = $(event.target).closest('a').attr('href')
    @router.navigate url, trigger: true
    false
