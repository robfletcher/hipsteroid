# Pages

* picture timeline
* upload picture
* view individual picture
* user profile

# Features

## Upload image

* generate filtered thumbs
* choose filter & submit to `PictureController.save`
* use asyncing to offload expensive image manipulation

# Cool things

* Testem
* Casper
* CoffeeScript
* Grunt / CodeKit
* dual rendering for optimized loading
* async expensive ops with vert.x & Rabbit MQ
* extending event bus to browser

# Basics

* design!
* pagination on timeline
* form validation

# Bugs

* it's not right that imageData can be null, can save be deferred until processing completes?
* non-authenticated user should not be able to go to `/upload`
* eventBus listener for thumbs is not unloaded
* eventBus listener for thumbs is not unique
* views are not unbound from events
* timeline should render in proper order & keep itself updated
* event bus URL is hardcoded in upload view
* timeline view can render images that aren't processed yet (possibly use startAsync but how will we know to resume?)

# Nice to haves

* comments & likes
* cache headers on restful actions
* no javascript support
* infinite scroll on timeline
* skip the link account page when signing in via twitter
* some kind of sensible persistent store for in-progress image uploads
* cloudbees jenkins running all the tests