UPDATE data_source_info
SET update_interval = 'P365D'
WHERE id = 'PORT_CALL_CODE_DESCRIPTIONS';

ALTER TABLE vessel
  ADD COLUMN IF NOT EXISTS created TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW(),
  ADD COLUMN IF NOT EXISTS modified TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW();

DROP TRIGGER IF EXISTS vessel_modified_t on vessel;
CREATE TRIGGER vessel_modified_t BEFORE UPDATE ON vessel FOR EACH ROW EXECUTE PROCEDURE update_modified_column_always();

create index if not exists vessel_modified_i on vessel USING btree(modified);

UPDATE updated_timestamp
set updated_name  = 'WINTER_NAVIGATION_VESSELS'
where updated_name = 'WINTER_NAVIGATION_SHIPS';

UPDATE updated_timestamp
set updated_name  = 'PORT_CALLS_TO'
where updated_name = 'PORT_CALLS';

ALTER TABLE cached_json
  ADD COLUMN IF NOT EXISTS modified TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW();

DROP TRIGGER IF EXISTS cached_json_update_modified_column_when_content_updated_t on cached_json;
CREATE TRIGGER cached_json_update_modified_column_when_content_updated_t BEFORE UPDATE ON cached_json FOR EACH ROW EXECUTE PROCEDURE update_modified_column_when_content_updated();
