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
    completed_date DATE,
    PRIMARY KEY (task_id)
);

INSERT IGNORE INTO study_tasks
(task_id, task_name, course_name, deadline, estimated_minutes, difficulty, importance, status, description, completed_date)
VALUES
(101, 'Complete Database Lab Report', 'Database Systems', '2026-07-27', 90, 'Medium', 'High', 'Pending', 'Complete SQL queries and prepare the lab report.', NULL),
(102, 'Study Operating System Notes', 'Operating System', '2026-07-29', 60, 'Easy', 'Medium', 'In Progress', 'Review process scheduling and deadlock topics.', NULL),
(103, 'Complete Machine Learning Assignment', 'Machine Learning', '2026-07-26', 120, 'Hard', 'High', 'Pending', 'Finish model implementation and report.', NULL),
(104, 'Read Design Pattern Chapter', 'Software Engineering', '2026-07-31', 45, 'Easy', 'Low', 'Completed', 'Read the strategy and factory pattern chapters.', '2026-07-25'),
(105, 'Prepare for CN Quiz', 'Computer Networks', '2026-07-28', 30, 'Easy', 'High', 'Completed', 'Revise TCP, UDP and DNS.', '2026-07-24'),
(106, 'Java Project Development', 'Advanced Java', '2026-07-25', 180, 'Hard', 'High', 'Pending', 'Complete Spring Boot CRUD project.', NULL);
