CREATE TABLE areatraffic(
  id BIGINT PRIMARY KEY NOT NULL,
  name TEXT NOT NULL,
  lighting_duration_min SMALLINT NOT NULL,
  geometry GEOMETRY NOT NULL
);
