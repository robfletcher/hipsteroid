Handlebars.registerHelper 'friendlyTime', (timestamp) ->
  m = moment timestamp
  new Handlebars.SafeString "<time datetime=\"#{m.format()}\">#{m.fromNow()}</time>"

Handlebars.registerHelper 'isCurrentUser', (user, body) ->
  if user.id is hipsteroid?.currentUser?.id
    console.log body
    body.fn()