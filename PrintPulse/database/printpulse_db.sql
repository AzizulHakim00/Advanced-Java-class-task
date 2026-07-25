CREATE DATABASE IF NOT EXISTS printpulse_db;
USE printpulse_db;

CREATE TABLE IF NOT EXISTS print_requests (
    request_id INT NOT NULL,
    student_name VARCHAR(80) NOT NULL,
    student_id VARCHAR(25) NOT NULL,
    document_name VARCHAR(120) NOT NULL,
    total_pages INT NOT NULL,
    copies INT NOT NULL,
    print_type VARCHAR(25) NOT NULL,
    paper_size VARCHAR(15) NOT NULL,
    status VARCHAR(20) NOT NULL,
    request_date DATE NOT NULL,
    collected_date DATE,
    notes VARCHAR(250),
    PRIMARY KEY (request_id)
);

INSERT IGNORE INTO print_requests
(request_id, student_name, student_id, document_name, total_pages, copies, print_type, paper_size, status, request_date, collected_date, notes)
VALUES
(1001, 'Rahim Ahmed', '2023-1-60-001', 'Database Lab Report', 20, 1, 'Black & White', 'A4', 'Waiting', CURDATE(), NULL, 'Staple the pages'),
(1002, 'Nusrat Jahan', '2023-2-60-014', 'Project Poster', 2, 1, 'Color', 'A3', 'Printing', CURDATE(), NULL, 'High quality color'),
(1003, 'Tanvir Hasan', '2022-3-60-044', 'Thesis Draft', 45, 2, 'Black & White', 'A4', 'Ready', CURDATE(), NULL, 'Double sided'),
(1004, 'Mim Akter', '2024-1-60-022', 'Presentation Handout', 8, 3, 'Color', 'A4', 'Collected', CURDATE(), CURDATE(), 'Collected from print desk');
