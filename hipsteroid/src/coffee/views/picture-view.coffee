class window.PictureView extends Backbone.View

  tagName: 'li'
  template: Handlebars.templates.picture
  events:
    'click button.delete': 'delete'

  initialize: (options) =>
    @model = options.model
    @listenTo @model, 'change', @render
    @listenTo @model, 'destroy', @remove
    @listenTo @model, 'remove', @remove

  render: =>
    @$el.append @template(@model.toJSON())
    @attach()

  attach: =>
    @

  delete: =>
    @model.destroy()
