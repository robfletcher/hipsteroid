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
    @model.on 'remove', @remove

  render: ->
    @$el.append @template(@model.toJSON())
    @attach()

  attach: ->
    @

  remove: ->
    @model.off 'change', @render
    @model.off 'destroy', @remove
    Backbone.View.prototype.remove.call @

  delete: ->
    @model.destroy()
