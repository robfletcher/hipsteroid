module.exports = (grunt) ->
  grunt.initConfig
    coffee:
      compile:
        expand: true
        cwd: 'src/coffee'
        src: ['**/*.coffee']
        dest: 'web-app/scripts'
        ext: '.js'
        options:
          bare: false

    compass:
      compile:
        options:
          sassDir: 'src/sass'
          cssDir: 'web-app/styles'
          imagesDir: 'web-app/images'
          importPath: ['lib/sass/semantic.gs']
          relativeAssets: true

    handlebars:
      compile:
        options:
          namespace: 'Handlebars.templates'
          processName: (filename) ->
            filename.match(/([^\/]+)\.hbs$/)[1]
        files:
          'web-app/scripts/templates.js': ['web-app/templates/*.hbs']

    watch:
      coffee:
        files: ['<config:coffee.compile.src>']
        tasks: ['coffee']

      compass:
        files: ['src/sass/**/*.less']
        tasks: ['compass']

      handlebars:
        files: ['web-app/templates/*.hbs']
        tasks: ['handlebars']

  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-compass'
  grunt.loadNpmTasks 'grunt-contrib-handlebars'
  grunt.registerTask 'default', ['coffee', 'compass', 'handlebars']
