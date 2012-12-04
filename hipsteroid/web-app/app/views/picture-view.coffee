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
    @attach()

  attach: ->
    @renderDeleteButton()
    @

  remove: ->
    @model.off 'change', @render
    @model.off 'destroy', @remove
    Backbone.View.prototype.remove.call @

  # TODO: put this in the handlebars template
  renderDeleteButton: ->
    @$el.find('figcaption').append('<button type="button" class="delete">Delete</button>') if hipsteroid.currentUser?.id is @model.get('uploadedBy').id

  delete: ->
    @model.destroy()
