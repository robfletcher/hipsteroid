1. Copy everything from `$VERTX_HOME/bin`, `$VERTX_HOME/conf` and `$VERTX_HOME/lib` to `vert.x/bin`, `vert.x/conf` and `vert.x/lib` respectively.
2. Download [CloudFoundry runtime jar](https://repo.springsource.org/simple/libs-milestone-s3-cache/org/cloudfoundry/cloudfoundry-runtime/0.8.2/cloudfoundry-runtime-0.8.2.jar) to `vert.x/lib`.
3. Build with `gradle build`.
4. Deploy with `vmc push hipsteroid-filter` specifying the start command `VERTX_MODS=build/work/mods vert.x/bin/vertx runmod hipsteroid-filter`
