# digitraffic-marine

## Development

### Prerequisites
1. Java 25 JDK
2. Maven
3. Git client
4. PostgreSQL database
5. Node.js 18+ (for build tooling)
6. Optional: MQTT server

You can run PostgreSQL and MQTT locally with Docker. See `dbmarine/README.md` and `mqttmarine/README.md`.

### Clone

```bash
git clone https://github.com/tmfg/digitraffic-marine-private.git
```

### Local profiles

Use one of these local profiles:

- `localhost-web` for the web app
- `localhost-daemon` for the daemon

The corresponding files are:

- `src/main/resources/application-localhost-web.properties`
- `src/main/resources/application-localhost-daemon.properties`

### Running locally

Start the local database first, then run the application with the desired profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=localhost-web
mvn spring-boot:run -Dspring-boot.run.profiles=localhost-daemon
```

Or run the packaged JAR:

```bash
java -Dspring.profiles.active=localhost-web -jar target/*.jar
java -Dspring.profiles.active=localhost-daemon -jar target/*.jar
```

### Notes

- `localhost-web` is for the web application only.
- `localhost-daemon` is for background jobs and integrations.
- The old `application-localhost.properties.template` has been removed.

### SchemaSpy

```bash
mvn exec:exec@schemaspy
```

Or:

```bash
cd dbmarine/schemaspy
./get-deps-and-run-schemaspy.sh [-o=/tmp/schema]
```

### Misc

Dependency updates:

```bash
mvn versions:display-dependency-updates
```

Dependency check:

```bash
mvn -Pdepcheck
```
