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

* dual rendering
	* Prevent redrawing when backbone views load
	* Pre-populate backbone model without requiring a server round-trip
* design!
* pagination on timeline
* form validation
* profile view
* grunt build
* no-op filter

# Bugs

* non-authenticated user should not be able to go to `/upload`
* timeline should keep itself updated
* event bus URL is hardcoded in upload view

# Nice to haves

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
