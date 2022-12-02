ALTER TABLE data_source_info
  ADD COLUMN IF NOT EXISTS recommended_fetch_interval TEXT DEFAULT 'PT5M' NOT NULL,
  ALTER COLUMN update_interval SET NOT NULL;

ALTER TABLE data_source_info
  DROP CONSTRAINT IF EXISTS recommended_fetch_interval_regexp_check,
  ADD CONSTRAINT recommended_fetch_interval_regexp_check
    CHECK (recommended_fetch_interval ~ '^([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?$');
