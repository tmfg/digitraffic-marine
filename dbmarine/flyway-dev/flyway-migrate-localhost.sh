#!/bin/sh
cd ../..
mvn flyway:migrate -Dflyway.configFile=dbmarine/flyway-dev/flyway-localhost.conf
