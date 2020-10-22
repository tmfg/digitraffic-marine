CREATE INDEX IF NOT EXISTS vessel_imo_timestamp_idx
    ON vessel
    USING BTREE (imo, timestamp DESC);
