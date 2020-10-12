CREATE SEQUENCE IF NOT EXISTS seq_portcall_estimates;

CREATE TABLE IF NOT EXISTS portcall_estimate (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_portcall_estimates'),
    event_type TEXT NOT NULL,
    event_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    event_time_confidence_lower TEXT,
    event_time_confidence_lower_diff INTEGER,
    event_time_confidence_upper TEXT,
    event_time_confidence_upper_diff INTEGER,
    event_source TEXT NOT NULL,
    record_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    ship_mmsi NUMERIC(10, 0),
    ship_imo NUMERIC(10, 0),
    location_locode CHARACTER VARYING(5) NOT NULL,
    location_terminal TEXT,
    location_berth TEXT,
    location_berth_position TEXT,
    location_ship_side TEXT
);

ALTER TABLE portcall_estimate DROP CONSTRAINT IF EXISTS portcall_estimate_type_check;
ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_type_check CHECK (event_type in ('ETA', 'ATB', 'ETD'));

ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_ship_mmsi_ship_imo_check CHECK (
        (ship_mmsi IS NOT NULL AND ship_imo IS NOT NULL) OR
        (ship_mmsi IS NOT NULL AND ship_imo IS NULL) OR
        (ship_mmsi IS NULL AND ship_imo IS NOT NULL)
);

CREATE INDEX IF NOT EXISTS portcall_estimate_locode_record_time_idx
    ON portcall_estimate
    USING BTREE (location_locode, record_time);

CREATE UNIQUE INDEX IF NOT EXISTS portcall_estimate_ship_id_event_source_event_time_rtime_idx
    ON portcall_estimate(ship_id, event_source, event_time, record_time);
