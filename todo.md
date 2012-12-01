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

# Features

* dual rendering
* design!
* pagination on timeline
* form validation
* profile view
* grunt build

# Bugs

* upload should wait for completion before returning to timeline
* timeline is in reverse order
* non-authenticated user should not be able to go to `/upload`
* eventBus listener for thumbs is not unique
* timeline should render in proper order & keep itself updated
* event bus URL is hardcoded in upload view

# Nice to haves

* use `@Secured` but have it send 401 instead of redirecting
* comments & likes
* cache headers on restful actions
* no javascript support
* infinite scroll on timeline
* skip the link account page when signing in via twitter
* cloudbees jenkins running all the tests