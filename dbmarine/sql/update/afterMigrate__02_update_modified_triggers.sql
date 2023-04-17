create or replace procedure ddl_create_modified_trigger_on_all_tables ()
  language plpgsql as $$
declare
  _sql varchar;
begin
  for _sql in
    select 'DROP TRIGGER IF EXISTS ' || table_name || '_modified_t on ' || table_name || '; ' ||
           'CREATE TRIGGER ' || table_name || '_modified_t BEFORE UPDATE ON ' || table_name || ' FOR EACH ROW EXECUTE PROCEDURE update_modified_column();' AS drop_and_create_trigger_query
    from information_schema.columns
    where table_schema = 'public'
    and column_name = 'modified'
    loop
      execute _sql;
    end loop;
end;
$$;

-- create before update trigger on all tables
call ddl_create_modified_trigger_on_all_tables();