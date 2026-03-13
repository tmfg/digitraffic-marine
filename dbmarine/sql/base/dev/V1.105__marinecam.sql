DO
$do$
BEGIN
  IF EXISTS (
   SELECT FROM pg_catalog.pg_user
   WHERE pg_user.usename = 'marinecam'
  ) THEN RAISE NOTICE 'User marinecam already exists';
  ELSE
    CREATE USER marinecam WITH PASSWORD 'marinecam';

    GRANT marinecam TO marine;

    CREATE SCHEMA marinecam AUTHORIZATION marinecam;

  END IF;
END
$do$;