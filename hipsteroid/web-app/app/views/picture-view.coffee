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

    @renderDeleteButton() if options.el?

  render: ->
    @$el.append @template(@model.toJSON())
    @renderDeleteButton()
    @

  remove: ->
    @model.off 'change', @render
    @model.off 'destroy', @remove
    Backbone.View.prototype.remove.call @

  renderDeleteButton: ->
    @$el.find('figcaption').append('<button type="button" class="delete">Delete</button>') if hipsteroid.currentUser?.id is @model.get('uploadedBy').id

  delete: ->
    @model.destroy()
