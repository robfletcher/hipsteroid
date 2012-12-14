module.exports = function(grunt) {

	grunt.initConfig({

		coffee: {
	    	app: {
	    		src: ['web-app/app/**/*.coffee'],
		        options: {
		            bare: false,
		            preserve_dirs: true
		        }
			}
	    },

	    less: {
		    app: {
				files: {
					'web-app/app/hipsteroid.css': 'web-app/app/hipsteroid.less'
				}
			}
		},

		handlebars: {
			app: {
				src: 'web-app/app/templates',
				dest: 'web-app/app/templates.js'
			}
		},

	    watch: {
	    	coffee: {
				files: ['<config:coffee.app.src>'],
				tasks: ['coffee:app']
			},
			less: {
				files: ['web-app/**/*.less'],
				tasks: ['less:app']
			},
			handlebars: {
				files: ['web-app/app/templates/*.handlebars'],
				tasks: ['handlebars:app']
			}
	    }

	});

	grunt.loadNpmTasks('grunt-coffee');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-handlebars');

	grunt.registerTask('default', ['coffee:app', 'less:app', 'handlebars:app']);

};
