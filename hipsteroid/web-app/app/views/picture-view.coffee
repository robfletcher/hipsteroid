class window.PictureView extends Backbone.View

  tagName: 'li'
  template: Handlebars.templates.picture
  events:
    'click button.delete': 'delete'

  initialize: (options) ->
    _.bindAll @
    @model = options.model
    @model.on 'change', @render
    @model.on 'destroy', @remove

  render: ->
    @$el.append @template(@model.toJSON())
    @renderDeleteButton() if window.currentUser?.id is @model.get('uploadedBy').id
    @

  remove: ->
    @model.off 'change', @render
    @model.off 'destroy', @remove
    Backbone.View.prototype.remove.call @

  renderDeleteButton: ->
    @$el.find('figcaption').append('<button type="button" class="delete">Delete</button>')

  delete: ->
    @model.destroy()
