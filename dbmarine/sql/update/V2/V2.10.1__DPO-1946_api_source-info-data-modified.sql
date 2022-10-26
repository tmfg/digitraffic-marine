ALTER TABLE vessel_location
  ADD COLUMN IF NOT EXISTS created TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW(),
  ADD COLUMN IF NOT EXISTS modified TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW();

DROP TRIGGER IF EXISTS vessel_location_modified_t on vessel_location;
CREATE TRIGGER vessel_location_modified_t BEFORE UPDATE ON vessel_location FOR EACH ROW EXECUTE PROCEDURE update_modified_column_always();

create index if not exists vessel_location_modified_i on vessel_location USING btree(modified);