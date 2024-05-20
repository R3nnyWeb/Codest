CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    method_name VARCHAR NOT NULL,
    drivers JSONB NOT NULL,
    start_code JSONB NOT NULL,
    languages VARCHAR[] NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    is_private BOOLEAN NOT NULL,
    description TEXT NOT NULL,
    level VARCHAR NOT NULL,
    input_types VARCHAR[] NOT NULL,
    output_type VARCHAR NOT NULL,
    user_id UUID NOT NULL
);

CREATE INDEX tasks_user_id_idx ON tasks (user_id);