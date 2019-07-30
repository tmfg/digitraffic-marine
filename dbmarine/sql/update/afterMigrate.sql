-- reindex after every db update
reindex table vessel;
reindex table vessel_location;
reindex table agent_info;
reindex table cargo_info;
reindex table port_area_details;
reindex table sse_report;
reindex table ssn_location;
reindex table updated_timestamp;
reindex table ship_activity;

-- reindex qrtz_tables
reindex table qrtz_fired_triggers;
reindex table qrtz_simple_triggers;
reindex table qrtz_triggers;
