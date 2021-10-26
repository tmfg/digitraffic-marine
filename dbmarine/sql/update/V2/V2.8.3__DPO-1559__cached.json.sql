create table if not exists cached_json (
     cache_id        text primary key,
     last_updated    timestamp(0) with time zone,
     content         jsonb
);