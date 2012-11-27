class window.PictureCollection extends Backbone.Collection

  model: Picture
  url: ->
    window.urlMappings.pictures

  comparator: (a, b) ->
    if a.dateCreated > b.dateCreated
      return -1
    else if a.dateCreated < b.dateCreated
      return 1
    return 0
