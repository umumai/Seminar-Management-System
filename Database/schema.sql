CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    role TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS submission (
    submission_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id TEXT,
    filepath TEXT,
    status TEXT
);

CREATE TABLE IF NOT EXISTS student_profile (
    student_id TEXT PRIMARY KEY,
    supervisor_name TEXT,
    research_title TEXT,
    abstract TEXT,
    presentation_type TEXT,
    FOREIGN KEY (student_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS session (
    session_id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_date TEXT,
    venue TEXT,
    session_type TEXT,
    time_slot TEXT
);

CREATE TABLE IF NOT EXISTS session_presenter (
    session_id INTEGER,
    student_id TEXt,
    PRIMARY KEY (session_id, student_id),
    FOREIGN KEY (session_id) REFERENCES session(session_id),
    FOREIGN KEY (student_id) REFERENCES student_profile(student_id)
);

CREATE TABLE IF NOT EXISTS session_evaluator (
    session_id INTEGER,
    evaluator_id INTEGER,
    PRIMARY KEY (session_id, evaluator_id),
    FOREIGN KEY (session_id) REFERENCES session(session_id),
    FOREIGN KEY (evaluator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS evaluation (
    evaluation_id INTEGER PRIMARY KEY AUTOINCREMENT,
    submission_id INTEGER,
    evaluator_id INTEGER,
    clarity_score INTEGER,
    methodology_score INTEGER,
    results_score INTEGER,
    presentation_score INTEGER,
    comments TEXT,
    FOREIGN KEY (submission_id) REFERENCES submission(submission_id),
    FOREIGN KEY (evaluator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS award (
    award_id INTEGER PRIMARY KEY AUTOINCREMENT,
    award_type TEXT,
    student_id TEXT,
    score REAL,
    FOREIGN KEY (student_id) REFERENCES student_profile(student_id)
);

CREATE TABLE IF NOT EXISTS report (
    report_id INTEGER PRIMARY KEY AUTOINCREMENT,
    report_type TEXT,
    generated_date TEXT
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id INTEGER,
    student_id INTEGER,
    evaluator_id INTEGER,
    time TEXT,
    status TEXT,
    FOREIGN KEY (session_id) REFERENCES session(session_id),
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (evaluator_id) REFERENCES users(id)
);

