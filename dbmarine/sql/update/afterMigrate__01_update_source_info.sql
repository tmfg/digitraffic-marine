INSERT INTO source_info (id, source, update_interval)
VALUES
      ('SSE_DATA', null, 'PT30M')

     ,('PORT_CALL', null, 'PT1M')
     ,('PORT_CALL_VESSEL_DETAIL', null, 'PT30M')
     ,('PORT_CALL_LOCATION', null, 'PT12H')
     ,('PORT_CALL_CODE_DESCRIPTIONS', null, null)

     ,('VESSEL_DETAIL', null, 'PT30M')
     ,('VESSEL_LOCATION', null, 'PT0M')

    ,('WINTER_NAVIGATION_PORT', null, 'PT5M')
    ,('WINTER_NAVIGATION_VESSEL', null, 'PT5M')
    ,('WINTER_NAVIGATION_DIRWAY', null, 'PT15M')

    ,('NAUTICAL_WARNING', null, 'PT10M')

    ,('BRIDGE_LOCK_DISRUPTION', null, 'PT10M')

    ,('ATON_FAULTS', null, 'PT0S')
ON CONFLICT (id) DO UPDATE
set source = EXCLUDED.source, update_interval = EXCLUDED.update_interval;