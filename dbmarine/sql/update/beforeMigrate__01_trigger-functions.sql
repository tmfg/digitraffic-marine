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

-- Updates modified column only when data in content column has changed
CREATE OR REPLACE FUNCTION update_modified_column_when_content_updated()
  RETURNS TRIGGER AS $$
BEGIN
  -- remove field from json with -
  IF (to_jsonb(OLD.content)) <> (to_jsonb(NEW.content)) THEN
    NEW.modified = now();
  END IF;
  RETURN NEW;
END;
$$ language 'plpgsql';