CREATE TABLE tests(
    id uuid primary key,
    task_id uuid references tasks(id),
    input_values text[],
    output_value text,
    FOREIGN KEY (task_id) REFERENCES tasks(id)
)