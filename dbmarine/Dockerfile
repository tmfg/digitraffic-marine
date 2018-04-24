FROM openjdk:8-jdk-slim

MAINTAINER Digitraffic

RUN \
  apt-get update && \
  apt-get -y install \
    maven \
    postgresql-client && \
  rm -rf /var/lib/apt/lists/*

COPY files/flyway.conf /flyway.conf
COPY files/pom.xml /pom.xml
COPY files/init-db.sh /init-db.sh
COPY files/wait-for-postgres.sh /wait-for-postgres.sh

