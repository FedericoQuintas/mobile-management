CREATE TABLE IF NOT EXISTS phone_events (
    model VARCHAR(20),
    username VARCHAR(30) NOT NULL,
    event_type VARCHAR(30) NOT NULL,
    event_time timestamp NOT NULL,
    version SMALLINT NOT NULL,
    UNIQUE (model, version)
);

CREATE INDEX IF NOT EXISTS index_model ON phone_events (model);