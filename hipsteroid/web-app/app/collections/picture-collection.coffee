class window.PictureCollection extends Backbone.Collection

  model: Picture
  url: ->
    hipsteroid.urlMappings.pictures

  comparator: (a, b) ->
    if a.get('dateCreated') > b.get('dateCreated')
      return -1
    else if a.get('dateCreated') < b.get('dateCreated')
      return 1
    return 0
