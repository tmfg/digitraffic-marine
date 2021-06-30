ALTER TABLE portcall_estimate ADD COLUMN IF NOT EXISTS ship_mmsi NUMERIC(10, 0);
ALTER TABLE portcall_estimate ADD COLUMN IF NOT EXISTS ship_imo NUMERIC(10, 0);

-- Update MMSI
UPDATE portcall_estimate SET ship_mmsi =
    (SELECT DISTINCT FIRST_VALUE(mmsi) OVER (ORDER BY timestamp DESC) FROM vessel WHERE mmsi = ship_id) WHERE ship_id_type = 'mmsi' AND ship_mmsi IS NULL;

UPDATE portcall_estimate SET ship_mmsi =
    (SELECT DISTINCT FIRST_VALUE(mmsi) OVER (ORDER BY timestamp DESC) FROM vessel WHERE imo = ship_id) WHERE ship_id_type = 'imo' AND ship_mmsi IS NULL;

UPDATE portcall_estimate SET ship_mmsi =
    (SELECT DISTINCT FIRST_VALUE(mmsi) OVER (ORDER BY timestamp DESC) FROM vessel WHERE imo = secondary_ship_id) WHERE secondary_ship_id_type = 'imo' AND ship_mmsi IS NULL;

UPDATE portcall_estimate SET ship_mmsi =
    (SELECT DISTINCT FIRST_VALUE(mmsi) OVER (ORDER BY port_call_timestamp DESC) FROM port_call WHERE imo_lloyds = ship_id) WHERE ship_id_type = 'imo' AND ship_mmsi IS NULL;

UPDATE portcall_estimate SET ship_mmsi =
    (SELECT DISTINCT FIRST_VALUE(mmsi) OVER (ORDER BY port_call_timestamp DESC) FROM port_call WHERE imo_lloyds = secondary_ship_id) WHERE secondary_ship_id_type = 'imo' AND ship_mmsi IS NULL;

-- Update IMO
UPDATE portcall_estimate SET ship_imo =
    (SELECT DISTINCT FIRST_VALUE(imo) OVER (ORDER BY timestamp DESC) FROM vessel WHERE imo = ship_id) WHERE ship_id_type = 'imo' AND ship_imo IS NULL;

UPDATE portcall_estimate SET ship_imo =
    (SELECT DISTINCT FIRST_VALUE(imo) OVER (ORDER BY timestamp DESC) FROM vessel WHERE mmsi = ship_id) WHERE ship_id_type = 'mmsi' AND ship_imo IS NULL;

UPDATE portcall_estimate SET ship_imo =
    (SELECT DISTINCT FIRST_VALUE(imo) OVER (ORDER BY timestamp DESC) FROM vessel WHERE imo = secondary_ship_id) WHERE secondary_ship_id_type = 'imo' AND ship_imo IS NULL;

UPDATE portcall_estimate SET ship_imo =
    (SELECT DISTINCT FIRST_VALUE(imo_lloyds) OVER (ORDER BY port_call_timestamp DESC) FROM port_call WHERE imo_lloyds = ship_id) WHERE ship_id_type = 'imo' AND ship_imo IS NULL;

UPDATE portcall_estimate SET ship_imo =
    (SELECT DISTINCT FIRST_VALUE(imo_lloyds) OVER (ORDER BY port_call_timestamp DESC) FROM port_call WHERE imo_lloyds = secondary_ship_id) WHERE secondary_ship_id_type = 'imo' AND ship_imo IS NULL;

DELETE FROM portcall_estimate WHERE ship_mmsi IS NULL OR ship_imo IS NULL;

ALTER TABLE portcall_estimate ALTER COLUMN ship_mmsi SET NOT NULL;
ALTER TABLE portcall_estimate ALTER COLUMN ship_imo SET NOT NULL;

ALTER TABLE portcall_estimate ALTER COLUMN ship_id DROP NOT NULL;
ALTER TABLE portcall_estimate ALTER COLUMN ship_id_type DROP NOT NULL;

DROP INDEX portcall_estimate_ship_id_event_source_event_time_rtime_idx;
CREATE UNIQUE INDEX IF NOT EXISTS portcall_estimate_mmsi_imo_evtsource_evttype_evttime_rtime_idx
    ON portcall_estimate(ship_mmsi, ship_imo, event_source, event_type, event_time, record_time);
