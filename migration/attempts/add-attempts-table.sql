CREATE TABLE IF NOT EXISTS attempts(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    task_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    code TEXT NOT NULL,
    language VARCHAR(50) NOT NULL,
    error TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE INDEX IF NOT EXISTS attempts_user_id_idx ON attempts(user_id);
CREATE INDEX IF NOT EXISTS attempts_task_id_idx ON attempts(task_id);
