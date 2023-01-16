ALTER TABLE aton_fault
  ADD COLUMN IF NOT EXISTS created TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW(),
  ADD COLUMN IF NOT EXISTS modified TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT NOW();

DROP TRIGGER IF EXISTS aton_fault_t on aton_fault;
CREATE TRIGGER aton_fault_t BEFORE UPDATE ON aton_fault FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

create index if not exists aton_fault_modified_i on aton_fault USING btree(modified);