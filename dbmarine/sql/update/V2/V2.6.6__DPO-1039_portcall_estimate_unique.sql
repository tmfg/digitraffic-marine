DROP INDEX portcall_estimate_mmsi_imo_esource_locode_etype_etime_rtime_idx;
CREATE UNIQUE INDEX IF NOT EXISTS portcall_estimate_update_key
    ON portcall_estimate(ship_mmsi, ship_imo, event_source, location_locode, event_type, event_time, record_time, portcall_id);

COMMENT ON INDEX portcall_estimate_update_key IS 'This unique key is used when inserting/updating to find conflicts';