# digitraffic-marine

## Development

### Preconditions
1. Java 17 JDK
2. Maven
3. Git client
4. Postgresql-database
5. Node.js 18 (for initializing Git submodules during build)
6. (optional) mqtt-server

You can run postgresql and mqtt in docker.  See dbmarine/README.md and mqttmarine/README.md for details.

### Clone project to your computer

	$ git clone https://github.com/tmfg/digitraffic-marine.git
	# Clones a repository to your computer

### Configure project

To configure project copy ***src/main/resources/application-localhost.template*** -file
as ***application-localhost.properties*** and configure it according to your environment.

### Build project

	$ mvn clean install

### Running the application

Before building application with tests enabled, start dbmarine instance.
See [dbmarine/README.md](dbmarine/README.md).

    # tunnel ie. port 18080 to server that has access to pooki
    $ ssh user@server -L18080:remote.server.ip:80

    # Start application
	$ mvn spring-boot:run -Dspring.profiles.active=localhost

	Some errors will show on the console, because of some integrations are not reachable. The
	integrations are configured

Or build the JAR file with:

	$ mvn clean package

 And run the JAR by typing:

 	$ java -Dspring.profiles.active=localhost -jar target/AIS-0.0.1-SNAPSHOT.jar

### Generate SchemaSpy schemas from the db with Maven

    $ mvn exec:exec@schemaspy

Generated schemas can be found at `dbmarine/schemaspy/schema` -directory    

Or with custom parameters.
    
    $ mvn exec:exec@schemaspy -Dexec.args="-o=/tmp/schema"

Or without Maven

    $ cd dbmarine/schemaspy
    $ get-deps-and-run-schemaspy.sh [-o=/tmp/schema]

### Misc commands

Check for Maven dependency updates

    $ mvn versions:display-dependency-updates

### GitFlow

See http://jeffkreeftmeijer.com/2010/why-arent-you-using-git-flow/
