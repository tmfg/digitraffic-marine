#!/bin/sh
cd ../..
mvn flyway:migrate -Dflyway.configFiles=dbmarine/flyway-dev/flyway-localhost.conf
