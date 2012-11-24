Handlebars.registerHelper 'friendlyTime', (timestamp) ->
  m = moment timestamp
  new Handlebars.SafeString "<time datetime=\"#{m.format()}\">#{m.fromNow()}</time>"