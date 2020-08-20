CREATE TABLE portcall_estimate (
    id BIGINT PRIMARY KEY,
    event_type TEXT NOT NULL,
    event_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    event_time_confidence_lower TIMESTAMP(0) WITH TIME ZONE,
    event_time_confidence_upper TIMESTAMP(0) WITH TIME ZONE,
    event_source TEXT NOT NULL,
    record_time TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    ship_id NUMERIC(10, 0) NOT NULL,
    ship_id_type CHARACTER VARYING(4) NOT NULL,
    secondary_ship_id NUMERIC(10, 0),
    secondary_ship_id_type CHARACTER VARYING(4),
    location_locode CHARACTER VARYING(5) NOT NULL,
    location_terminal TEXT,
    location_berth TEXT,
    location_berth_position TEXT,
    location_ship_side TEXT
);

ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_type CHECK (event_type in ('ETA', 'ATB', 'ETD'));

ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_ship_id_type CHECK (ship_id_type in ('mmsi', 'imo'));

ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_seconday_ship_id_type CHECK (secondary_ship_id_type in ('mmsi', 'imo'));

CREATE INDEX portcall_estimate_locode
    ON portcall_estimate
    USING BTREE (location_locode);

CREATE UNIQUE INDEX portcall_estimate_shipid_evtsource_evttime
    ON portcall_estimate(ship_id, event_source, event_time);
