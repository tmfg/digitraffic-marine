DROP TABLE IF EXISTS winter_navigation_dirway_point;
DROP TABLE IF EXISTS winter_navigation_dirway;

DROP TABLE IF EXISTS ship_state;
DROP TABLE IF EXISTS ship_voyage;
DROP TABLE IF EXISTS ship_activity;
DROP SEQUENCE IF EXISTS seq_ship_activity;
DROP TABLE IF EXISTS ship_planned_activity;
DROP SEQUENCE IF EXISTS seq_ship_planned_activity;
DROP TABLE IF EXISTS winter_navigation_ship;

DROP TABLE IF EXISTS port_restriction;
DROP SEQUENCE IF EXISTS seq_port_restriction;
DROP TABLE IF EXISTS winter_navigation_port;

DELETE FROM updated_timestamp
WHERE updated_name IN (
                       'WINTER_NAVIGATION_DIRWAYS',
                       'WINTER_NAVIGATION_DIRWAYS_CHECK',
                       'WINTER_NAVIGATION_PORTS',
                       'WINTER_NAVIGATION_PORTS_CHECK',
                       'WINTER_NAVIGATION_SHIPS',
                       'WINTER_NAVIGATION_VESSELS',
                       'WINTER_NAVIGATION_VESSELS_CHECK'
  );

DELETE FROM data_source_info
WHERE id IN (
             'WINTER_NAVIGATION_PORT',
             'WINTER_NAVIGATION_VESSEL',
             'WINTER_NAVIGATION_DIRWAY'
  );
