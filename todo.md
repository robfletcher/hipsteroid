# Pages

* picture timeline
* upload picture
* view individual picture
* user profile

# Cool things

* Testem
* Casper
* CoffeeScript
* Grunt / CodeKit
* dual rendering for optimized loading
* async expensive ops with vert.x & Rabbit MQ
* extending event bus to browser

# Features

* push updates to timeline
  * tests!
* design!
* grunt build
* pagination on timeline
* form validation
* profile view
  * avatars from twitter
* no-op filter

# Bugs

* nav links don't work from auth page
* really need 304s for images
* timeline view renders twice when route triggers, once & then again as REST call comes back (might not be a problem once push updates work)
* non-authenticated user should not be able to go to `/upload`
* event bus URL is hardcoded in upload view

# Nice to haves

* convert /login/auth to a backbone route
* drop jQuery file upload
* drop jQuery. Organic JS!
* handle errors in an AJAX sensitive way
* comments & likes
* cache headers on restful actions
* no javascript support
* infinite scroll on timeline
* skip the link account page when signing in via twitter
* cloudbees jenkins running all the tests
* run Casper & Testem in IntelliJ

# Mention but don't demo

* Pre-rendering Angular using Phantom