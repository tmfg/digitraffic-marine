DROP TABLE IF EXISTS cached_json;

DELETE FROM updated_timestamp
WHERE updated_name IN (
    'NAUTICAL_WARNINGS_CHECK',
    'NAUTICAL_WARNINGS'
);

DELETE FROM data_source_info
WHERE id = 'NAUTICAL_WARNING';
