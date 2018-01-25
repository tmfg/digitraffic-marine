FROM java:8

MAINTAINER Digitraffic

RUN \
  apt-get update && \
  apt-get -y install \
    maven && \
  rm -rf /var/lib/apt/lists/*

COPY sql /sql
COPY flyway.properties /flyway.properties
COPY pom.xml /pom.xml
ENTRYPOINT ["mvn", "flyway:migrate"]