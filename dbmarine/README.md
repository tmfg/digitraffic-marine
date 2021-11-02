# Digitraffic marine database


## ;TL;TR

````bash
docker-compose rm db && docker-compose build && docker-compose up
````
## Hakemistorakenne

    sql
    └ base       -> Juuri tyhjä
    |   └ common -> Kaikille ympäristöille yhteiset alustukset, mm. kaikki taulujen teot ennen V2-versioita
    |   └ dev    -> Kehityskäytössä käytettäviä alustuksia.
    |   └ prod   -> Tuotantoon ja testiin ajetut V1 alustukset, joita ei tarvita kehityskäytössä. 
    |               Näitä ei voi heittää mäkeen, koska flyway vaatii historian säilymisen.
    |
    └ update     -> Juuressa aina ajettavia huoltoskriptejä.
    └ V2     -> Kaikki päivitykset kantaan V1-version jälkeen.

## Running db instance

````bash
docker-compose build && docker-compose up
````

PostgreSql db is running at localhost:54321 and adminer at localhost:8081

List containers
``````bash
docker-compose ps
``````

Removing containers
``````bash
docker-compose rm db
``````

In case building PostGIS fails because of unknown version:
1. git clone https://github.com/appropriate/docker-postgis.git
2. cd docker-postgis && ./update.sh
3. Grab the next version from the script output, example: `s/%%POSTGIS_VERSION%%/2.5.4+dfsg-1.pgdg90+1/g;` The bit you need is *2.5.4+dfsg-1.pgdg90+1*
4. Update the variable *POSTGIS_VERSION* in *postgis/Dockerfile_postgis*

Running update.sh on OSX:  
- remove the `-f` from `readlink -f`
- run the script with bash, zsh etc. don't work

If that is not working, then see https://github.com/postgis/docker-postgis
and pick right Postgres version and DockerFile and pick POSTGIS_MAJOR and POSTGIS_VERSION.

If postgis extension is not initialized run following commands manually:
``````bash
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder;
``````