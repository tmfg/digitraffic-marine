DO
$do$
BEGIN
  IF EXISTS (
   SELECT FROM pg_catalog.pg_user
   WHERE pg_user.usename = 'portactivity'
  ) THEN RAISE NOTICE 'User portactivity already exists';
  ELSE
    CREATE USER portactivity WITH PASSWORD 'portactivity';

    GRANT portactivity TO marine;

    CREATE SCHEMA portactivity AUTHORIZATION portactivity;

    GRANT SELECT,INSERT,UPDATE,DELETE ON public.vessel TO portactivity;
    GRANT SELECT,INSERT,UPDATE,DELETE ON public.port_call TO portactivity;
    GRANT SELECT,INSERT,UPDATE,DELETE ON public.port_area_details TO portactivity;
    GRANT SELECT,INSERT,UPDATE,DELETE ON public.vessel_location TO portactivity;
  END IF;
END
$do$;