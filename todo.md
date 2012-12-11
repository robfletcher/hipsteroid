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
* grunt build
* pagination on timeline
* profile view
  * avatars from twitter

# Bugs

* really need 304s for images
* timeline view renders twice when route triggers, once & then again as REST call comes back (might not be a problem once push updates work)
* delete is assumed to succeed. Could we silence the event and wait until vert.x notifies the page?

# Nice to haves

* compiled js & css in different dirs so it can be ignored more easily
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