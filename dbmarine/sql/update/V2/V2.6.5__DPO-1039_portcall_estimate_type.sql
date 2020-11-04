ALTER TABLE portcall_estimate DROP CONSTRAINT IF EXISTS portcall_estimate_type_check;
ALTER TABLE portcall_estimate
    ADD CONSTRAINT portcall_estimate_type_check CHECK (event_type in ('ETA', 'ATB', 'ETD', 'ATA'));
