# Hipsteroid

This is a demo app written for the talk [Grails for Hipsters][talk] at [Groovy & Grails Exchange 2012][ggx]. It is a simple _Instagram_-like photo sharing app.

The slides for the talk can be found on [SlideShare](http://www.slideshare.net/rfletcher/grails-for-hipsters) and the video is available via [SkillsMatter](http://skillsmatter.com/podcast/groovy-grails/grails-for-hipsters).

## Structure

* `filter` contains a vert.x application that does image processing.
* `hipsteroid` contains a Grails application that controls workflow.

## Things you need to run this demo

* [Grails 2.1.2][grails]
* [Vert.x][vert.x]
* [MongoDB][mongo]
* [ImageMagick][im] (you may have this already - try running `convert` in a terminal)

Some way to compile…

* [CoffeeScript][coffee]
* [LESS][less]
* [Handlebars][hbs]

## Compiling front end resources

### CodeKit

There is a [CodeKit][codekit] config file in `web-app/app` that will allow you to automatically compile CoffeeScript and LESS. You will still need to compile the Handlebars templates another way as CodeKit does not process them.

### Grunt

To run the [Grunt][grunt] build you need to first run `npm install` (just once) to pull in the various Grunt plugins. Then you can execute:

	grunt

To compile CoffeeScript, LESS & Handlebars. Or:

	grunt watch

To monitor for changes in those resources and automatically compile them if they change.

### Command line:

To install the command line tools:

	npm install -g coffee-script
	gem install less
	npm install -g handlebars

Then use the following commands to compile the resources:

	coffee -c web-app/app
	lessc web-app/app/hipsteroid.less > web-app/app/hipsteroid.css
	handlebars web-app/app/templates/ -f web-app/app/templates.js

## To run the app

You need to run 3 things simultaneously. I just use 3 terminal tabs.

### The vert.x app

	cd filter
	./gradlew build runMod

### The grails app

	cd hipsteroid
	grails run-app

### MongoDB

	mongod

## Tests

Unit tests are standard Grails / Spock tests. Javascript tests use [Testem][testem] (`npm install -g testem`), End-to-end tests use [Casper JS][casper] (`brew install casperjs` on a Mac or download from site).

### To run JS unit tests (continuous test mode)

	cd hipsteroid
	testem

Then just point a browser (or multiple browsers) at `http://localhost:7357/`.

### To run JS unit tests (continuous integration mode)

	cd hipsteroid
	testem ci

### To run end-to-end tests

The app must be running before you do this.

	cd hipsteroid
	casperjs test test/end-to-end/

[talk]:http://skillsmatter.com/podcast/groovy-grails/grails-for-hipsters
[ggx]:http://skillsmatter.com/event/groovy-grails/groovy-grails-exchange-2012
[grails]:http://grails.org/
[vert.x]:http://vertx.io/
[mongo]:http://www.mongodb.org/
[im]:http://www.imagemagick.org/script/index.php
[coffee]:http://coffeescript.org/
[less]:http://lesscss.org/
[hbs]:http://handlebarsjs.com/
[codekit]:http://incident57.com/codekit/
[testem]:https://github.com/airportyh/testem
[casper]:http://casperjs.org/
[grunt]:http://gruntjs.org/