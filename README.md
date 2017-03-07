# digitraffic-ais

## Development

###Preconditions
1. Java 1.8 JDK
2. Maven
3. Git client
4. Oracle 11g database

### Clone project to your computer

	$ git clone https://github.com/finnishtransportagency/digitraffic-ais.git
	# Clones a repository to your computer

### Configure project

To configure project copy ***src/main/resources/application-localhost.template*** -file
as ***application-localhost.properties*** and configure it according to your environment.

### Configure Oracle JDBC driver

Download Oracle JDBC driver and add it to your local Maven repository.

	$ mvn install:install-file -DgroupId=oracle -DartifactId=ojdbc7 \
	  -Dversion=12.1.0.2 -Dpackaging=jar  -DgeneratePom=true -Dfile=ojdbc7-12.1.0.2.jar

**Or** add Maven repository that contains OJDBC-driver to project's pom.xml inside repositories-tag.


### Build project

	$ mvn clean install

### Running the application

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

### Misc commands

Check for Maven dependency updates

    $ mvn versions:display-dependency-updates

### GitFlow

See http://jeffkreeftmeijer.com/2010/why-arent-you-using-git-flow/

