module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
            browserSync: {
            dev: {
                bsFiles: {
                    src : [
                        'smwc/src/main/webapp/**/*.html',
                        'smwc/src/main/webapp/**/*.js',
                        'smwc/src/main/webapp/**/*.css'
                    ]
                },
              options: {
                  proxy: "localhost:8080"
              }
        }},
    connect: {
      options: {
        port: 9000,
        hostname: 'localhost',
        debug: true,
        keepalive: true,
        base: 'smwc/src/main/webapp'
      },
      server: {
        options: {
          port: 9000,
          hostname: 'localhost'
        },
        proxies: [
          {
            //all requests under / will be handled by this proxy
            context: '/',
            host: 'localhost',
            port: 8080,
            https: false,
            xforward: false,
          }
        ]       
      },
        livereload: {
          options: {
            middleware: function(connect, options) {
                if (!Array.isArray(options.base)) {
                  options.base = [options.base];
                }

                //set up proxy
                var middlewares = [require('grunt-connect-proxy/lib/utils').proxyRequest];

                //serve static files
                options.base.forEach(function(base) {
                  middlewares.push(connect.static(base));
                  console.log(base);
                });

                //make directory browse-able
                var directory = options.directory || options.base[options.base.length - 1];
                middlewares.push(connect.directory(directory));

                return middlewares;
            }
          }
        }
    }
  });

  grunt.loadNpmTasks('grunt-connect-proxy');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-browser-sync');


  grunt.registerTask('server', function(target) {
    grunt.task.run([
            'configureProxies:server',
            'livereload-start',
            'connect:livereload'
      ]);
  });
    grunt.registerTask('browser', function(target) {
    grunt.task.run([
            'browserSync'
      ]);
  });

};