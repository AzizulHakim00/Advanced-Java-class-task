CREATE DATABASE IF NOT EXISTS focusforge_db;
USE focusforge_db;

CREATE TABLE IF NOT EXISTS study_tasks (
    task_id INT NOT NULL,
    task_name VARCHAR(100) NOT NULL,
    course_name VARCHAR(60) NOT NULL,
    deadline DATE NOT NULL,
    estimated_minutes INT NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    importance VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(300),
    PRIMARY KEY (task_id)
);

-- Optional sample data. Run only when you need demo records.
-- INSERT INTO study_tasks
-- (task_id, task_name, course_name, deadline, estimated_minutes, difficulty, importance, status, description)
-- VALUES
-- (101, 'Complete Spring Boot Assignment', 'Advanced Java', '2026-08-05', 90, 'Hard', 'High', 'Pending', 'Finish controller and Thymeleaf pages'),
-- (102, 'Review Database Notes', 'Database Systems', '2026-08-02', 45, 'Medium', 'Medium', 'In Progress', 'Review SQL joins and normalization');
