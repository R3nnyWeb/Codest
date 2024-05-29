CREATE TABLE IF NOT EXISTS users_competitions(
    competition_id uuid references competitions(id),
    user_id uuid references users(id),
    PRIMARY KEY (competition_id, user_id)
)
