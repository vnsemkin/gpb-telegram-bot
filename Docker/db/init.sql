CREATE EXTENSION IF NOT EXISTS dblink;

DO
    $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_database
        WHERE datname = 'gpb'
    ) THEN
        PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE gpb');
END IF;
END
$$;