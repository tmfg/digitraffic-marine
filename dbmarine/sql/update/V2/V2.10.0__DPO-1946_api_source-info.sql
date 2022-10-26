CREATE TABLE IF NOT EXISTS source_info (
  id              TEXT  PRIMARY KEY,
  source          TEXT, -- not in use yet
  update_interval TEXT, -- PnDTnHnMn.nS
  UNIQUE (id)
);

-- Pattern is format of “PnDTnHnMn.nS”. Regexp is taken from Duration.java
ALTER TABLE source_info
  DROP CONSTRAINT IF EXISTS update_interval_regexp_check,
  ADD CONSTRAINT update_interval_regexp_check
  CHECK (update_interval ~ '^([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?$');