FROM java:8

MAINTAINER Digitraffic

RUN \
  apt-get update && \
  apt-get -y install \
    maven \
    postgresql-client && \
  rm -rf /var/lib/apt/lists/*

COPY flyway.conf /flyway.conf
COPY pom.xml /pom.xml
COPY init-db.sh /init-db.sh
COPY wait-for-postgres.sh /wait-for-postgres.sh

ADD files
COPY wait-for-postgres.sh files/wait-for-postgres.sh
