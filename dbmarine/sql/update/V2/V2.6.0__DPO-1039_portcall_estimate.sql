CREATE TABLE portcall_estimate (
    id BIGINT PRIMARY KEY,
    event_id TEXT NOT NULL,
    event_type TEXT NOT NULL,
    event_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    event_time_confidence_lower TEXT,
    event_time_confidence_upper TEXT,
    event_source TEXT NOT NULL,
    record_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    ship_mmsi NUMERIC(10,0),
    ship_imo NUMERIC(7,0),
    location_port TEXT,
    location_terminal TEXT,
    location_berth TEXT,
    location_berth_position TEXT,
    location_ship_side TEXT
);

ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_type CHECK (event_type in ('ETA', 'ATB', 'ETD'));
