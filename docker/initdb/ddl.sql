DROP TABLE IF EXISTS room_booking_details CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS borrow_records CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS progress CASCADE;
DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS librarians CASCADE;
DROP TABLE IF EXISTS instructors CASCADE;
DROP TABLE IF EXISTS administrators CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS course_level CASCADE;
DROP TYPE IF EXISTS room_type CASCADE;
DROP TYPE IF EXISTS course_type CASCADE;


CREATE TYPE user_role AS ENUM ('administrator', 'instructor', 'librarian', 'student');

CREATE TYPE course_level AS ENUM ('beginner', 'intermediate', 'advanced');

CREATE TYPE room_type AS ENUM ('classroom', 'lab', 'lecture hall', 'seminar room', 'exam room', 'other');

CREATE TYPE course_type AS ENUM ('video based', 'text based', 'audio based', 'slides', 'quiz', 'assignments');


CREATE TABLE users (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    surname         VARCHAR(100) NOT NULL,
    email           VARCHAR(150) UNIQUE NOT NULL,
    username        VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    role            user_role NOT NULL
);

CREATE TABLE administrators (
    user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE instructors (
    user_id         INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    bio           TEXT,
    profession      VARCHAR(200),
    experience      INT
);

CREATE TABLE librarians (
    user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE students (
    user_id        INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    university     VARCHAR(200),
    uni_id         VARCHAR(100),
    card_details   VARCHAR(255)
);


CREATE TABLE courses (
    course_id           SERIAL PRIMARY KEY,
    instructor_id       INT REFERENCES instructors(user_id) ON DELETE SET NULL,
    course_name         VARCHAR(255) NOT NULL,
    type                course_type NOT NULL,
    category            VARCHAR(100),
    satisfaction_factor DOUBLE PRECISION,
    review_number       INT,
    duration            TIME,
    num_of_lectures     INT,
    level               course_level NOT NULL
);


CREATE TABLE lessons (
    lesson_id       SERIAL PRIMARY KEY,
    course_id       INT NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    title           VARCHAR(255),
    duration        TIME,
    is_completed    BOOLEAN DEFAULT FALSE
);


CREATE TABLE enrollments (
    student_id      INT REFERENCES students(user_id) ON DELETE CASCADE,
    course_id       INT REFERENCES courses(course_id) ON DELETE CASCADE,
    enroll_date     DATE DEFAULT CURRENT_DATE,
    completion_rate NUMERIC(5,2),
    completed_lessons INT DEFAULT NULL,
    PRIMARY KEY(student_id, course_id)
);


CREATE TABLE reviews (
    review_id   SERIAL PRIMARY KEY,
    student_id  INT REFERENCES students(user_id) ON DELETE CASCADE,
    course_id   INT REFERENCES courses(course_id) ON DELETE CASCADE,
    rating      INT CHECK (rating BETWEEN 1 AND 10),
    comment     TEXT,
    date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE books (
    book_id     SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255),
    language    VARCHAR(100),
    genre       VARCHAR(100),
    available   BOOLEAN DEFAULT TRUE
);


CREATE TABLE borrow_records (
    borrow_id       SERIAL PRIMARY KEY,
    student_id      INT REFERENCES students(user_id) ON DELETE CASCADE,
    book_id         INT REFERENCES books(book_id) ON DELETE CASCADE,
    borrow_date     DATE NOT NULL,
    end_date        DATE,
    due_date        DATE,
    is_overdue      BOOLEAN DEFAULT FALSE
);


CREATE TABLE rooms (
    room_id     SERIAL PRIMARY KEY,
    room_number INT NOT NULL UNIQUE,
    floor       INT NOT NULL,
    capacity    INT NOT NULL,
    type        room_type DEFAULT 'other',
    available   BOOLEAN DEFAULT TRUE
);


CREATE TABLE room_booking_details (
    booking_id  SERIAL PRIMARY KEY,
    room_id     INT REFERENCES rooms(room_id) ON DELETE CASCADE,
    student_id  INT REFERENCES students(user_id) ON DELETE CASCADE,
    date        DATE NOT NULL,
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    active      BOOLEAN DEFAULT TRUE
);
