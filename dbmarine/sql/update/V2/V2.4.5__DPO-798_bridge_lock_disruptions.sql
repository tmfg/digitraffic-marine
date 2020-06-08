CREATE TABLE bridgelock_disruption
(
    id      BIGINT PRIMARY KEY NOT NULL,
    type_id BIGINT NOT NULL,
    start_date         TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    end_date           TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    geometry           GEOMETRY,
    description_fi     TEXT,
    description_sv     TEXT,
    description_en     TEXT
);
