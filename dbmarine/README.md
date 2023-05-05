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
