CREATE TABLE IF NOT EXISTS tasks_competitions(
    competition_id uuid references competitions(id),
    task_id uuid references tasks(id),
    points int NOT NULL,
    PRIMARY KEY (competition_id, task_id)
)