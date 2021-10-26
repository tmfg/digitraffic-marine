CREATE TABLE areatraffic(
  id BIGINT PRIMARY KEY NOT NULL,
  name TEXT NOT NULL,
  brighten_duration_min SMALLINT NOT NULL,
  geometry GEOMETRY NOT NULL,
  brighten_sent TIMESTAMP WITH TIME ZONE,
  brighten_end TIMESTAMP WITH TIME ZONE
);
