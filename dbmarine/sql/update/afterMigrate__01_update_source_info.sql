INSERT INTO data_source_info (id, source, update_interval, recommended_fetch_interval)
VALUES
      ('SSE_DATA', null, 'PT30M', 'PT5M')

     ,('PORT_CALL', null, 'PT1M', 'PT1M')
     ,('PORT_CALL_VESSEL_DETAIL', null, 'PT30M', 'PT5M')
     ,('PORT_CALL_LOCATION', null, 'PT12H', 'PT1H')
     ,('PORT_CALL_CODE_DESCRIPTIONS', null, 'P365D', 'P1D')

     ,('VESSEL_DETAIL', null, 'PT30M', 'PT5M')
     ,('VESSEL_LOCATION', null, 'PT0M', 'PT1M')

    ,('WINTER_NAVIGATION_PORT', null, 'PT5M', 'PT1M')
    ,('WINTER_NAVIGATION_VESSEL', null, 'PT5M', 'PT1M')
    ,('WINTER_NAVIGATION_DIRWAY', null, 'PT15M', 'PT5M')

    ,('NAUTICAL_WARNING', null, 'PT10M', 'PT5M')

    ,('BRIDGE_LOCK_DISRUPTION', null, 'PT10M', 'PT5M')

    ,('ATON_FAULTS', null, 'PT0S', 'PT1M')
ON CONFLICT (id) DO UPDATE
set source = EXCLUDED.source, update_interval = EXCLUDED.update_interval;