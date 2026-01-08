CREATE TABLE IF NOT EXISTS submission (
    submission_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER,
    filepath TEXT,
    status TEXT
);
