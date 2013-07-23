# Pages

* view individual picture
* user profile
* make auth stuff properly one-page

# Cool things

* Upgrade filter to use Vert.x 2
* Use Karma rather than Testem
* Use Grunt to watch & recompile resources when you use `grails run-app`

# Bugs

* really need 304s for images
* timeline view renders twice when route triggers, once & then again as REST call comes back (might not be a problem once push updates work)
* delete is assumed to succeed. Could we silence the event and wait until vert.x notifies the page?

# Nice to haves

* drop jQuery file upload
* drop jQuery. Organic JS!
* handle errors in an AJAX sensitive way
* comments & likes
* cache headers on restful actions
* no javascript support
* infinite scroll on timeline
* cloudbees jenkins running all the tests
* run tests in IntelliJ
