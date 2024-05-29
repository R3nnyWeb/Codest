CREATE TABLE IF NOT EXISTS competitions(
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE attempts ADD COLUMN IF NOT EXISTS competition_id UUID REFERENCES competitions(id);
