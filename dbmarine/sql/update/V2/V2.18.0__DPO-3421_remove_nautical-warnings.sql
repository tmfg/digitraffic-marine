DROP TRIGGER IF EXISTS cached_json_update_modified_column_when_content_updated_t;

DROP TABLE IF EXISTS cached_json;

DELETE FROM updated_timestamp
WHERE updated_name IN (
    'NAUTICAL_WARNINGS_CHECK',
    'NAUTICAL_WARNINGS'
);

DELETE FROM data_source_info
WHERE id = 'NAUTICAL_WARNING';
