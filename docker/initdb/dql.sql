-- Register a new student user
INSERT INTO users (name, surname, email, username, password, role)
VALUES ('John', 'Doe', 'john@example.com', 'john_doe', 'password', 'student');

-- Log in
SELECT id, password, role
FROM users
WHERE username = 'john_doe';

-- View all available courses
SELECT * FROM courses;

-- Enroll in a course
INSERT INTO enrollments (student_id, course_id)
VALUES (123, 10);

-- Get lessons for a course the student is taking
SELECT l.lesson_id, l.title, l.duration, l.is_completed
FROM lessons l
JOIN enrollments e ON e.course_id = l.course_id
WHERE l.course_id = 10 AND e.student_id = 123;

-- Mark a lesson as completed
UPDATE lessons
SET is_completed = TRUE
WHERE lesson_id = 55;

-- Update student course progress
UPDATE enrollments
SET completed_lessons = completed_lessons + 1,
    completion_rate = ROUND(
        ((completed_lessons + 1) /
        (SELECT num_of_lectures FROM courses WHERE course_id = 10)) * 100, 2
    )
WHERE student_id = 123 AND course_id = 10;

-- Submit review for a course
INSERT INTO reviews (student_id, course_id, rating, comment)
VALUES (123, 10, 9, 'Great course!');

-- Browse library catalog
SELECT * FROM books;

-- Borrow a book and mark book as unavailable
INSERT INTO borrow_records (student_id, book_id, borrow_date, due_date)
VALUES (123, 50, CURRENT_DATE, CURRENT_DATE + INTERVAL '14 day');

UPDATE books
SET available = FALSE
WHERE book_id = 50;

-- Return a borrowed book and mark book as available again
UPDATE borrow_records
SET end_date = CURRENT_DATE,
    is_overdue = CASE WHEN CURRENT_DATE > due_date THEN TRUE ELSE FALSE END
WHERE borrow_id = 200 AND student_id = 123;

UPDATE books
SET available = TRUE
WHERE book_id = (SELECT book_id FROM borrow_records WHERE borrow_id = 200);

-- View borrowing history of the student
SELECT br.borrow_id, b.title, br.borrow_date, br.due_date, br.end_date, br.is_overdue
FROM borrow_records br
JOIN books b ON br.book_id = b.book_id
WHERE br.student_id = 123;

-- View student’s room bookings
SELECT *
FROM room_booking_details
WHERE student_id = 123 AND active = TRUE;

-- Cancel a room booking
UPDATE room_booking_details
SET active = FALSE
WHERE booking_id = 500;

-- Edit an existing course
UPDATE courses
SET course_name = 'New Name', category = 'New Category'
WHERE course_id = 10 AND instructor_id = 777;

-- Delete course
DELETE FROM courses
WHERE course_id = 10 AND instructor_id = 777;

-- Add a room
INSERT INTO rooms (room_number, floor, capacity, type, available)
VALUES (205, 2, 30, 'classroom', TRUE);

-- View students enrolled in particular course
SELECT s.user_id, u.name, u.surname, e.enroll_date, e.completion_rate
FROM enrollments e
JOIN students s ON s.user_id = e.student_id
JOIN users u ON u.id = s.user_id
WHERE e.course_id = 10;

-- View all overdue items
SELECT br.*, u.name, u.surname, b.title
FROM borrow_records br
JOIN students s ON br.student_id = s.user_id
JOIN users u ON u.id = s.user_id
JOIN books b ON br.book_id = b.book_id
WHERE br.is_overdue = TRUE AND br.end_date IS NULL;


-- Most borrowed books report
SELECT b.title, COUNT(*) AS borrow_count
FROM borrow_records br
JOIN books b ON b.book_id = br.book_id
GROUP BY b.title
ORDER BY borrow_count DESC;

-- Books due tomorrow
SELECT student_id, book_id, due_date
FROM borrow_records
WHERE due_date = CURRENT_DATE + INTERVAL '1 day';
