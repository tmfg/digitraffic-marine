-- Remove bridge lock disruptions feature (DPO-798 deprecated 2026-04-20)
DROP TABLE IF EXISTS bridgelock_disruption;

DELETE FROM data_source_info WHERE id = 'BRIDGE_LOCK_DISRUPTION';
DELETE FROM updated_timestamp WHERE updated_name LIKE 'BRIDGE_LOCK_DISRUPTIONS%';