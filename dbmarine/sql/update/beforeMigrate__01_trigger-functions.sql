-- Updates modified column only when data has changed
CREATE OR REPLACE FUNCTION update_modified_column()
  RETURNS TRIGGER AS $$
BEGIN
  IF (to_jsonb(OLD.*) <> to_jsonb(NEW.*)) THEN
    NEW.modified = now();
  END IF;
  RETURN NEW;
END;
$$ language 'plpgsql';


-- Updates modified column also when data has not changed
CREATE OR REPLACE FUNCTION update_modified_column_always()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.modified = now();
  RETURN NEW;
END;
$$ language 'plpgsql';