package com.educationalSystem.dao;

import com.db.JDBCUtil;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseDAO {

    private final JDBCUtil db;

    public CourseDAO(JDBCUtil db) {
        this.db = db;
    }


    public Optional<Course> findById(Long id) {
        Optional<Course> course = db.findOne(
                """
                SELECT c.course_id, c.course_name, c.type, c.category,
                       c.satisfaction_factor, c.review_number,
                       c.duration, c.num_of_lectures, c.level,
                       i.user_id AS instructor_id, i.bio, i.profession, i.experience,
                       u.name, u.surname, u.email, u.username
                FROM courses c
                LEFT JOIN instructors i ON c.instructor_id = i.user_id
                LEFT JOIN users u ON u.id = i.user_id
                WHERE c.course_id = ?
                """,
                this::mapCourseRow,
                id
        );

        if (course == null) return null;

        List<Lesson> lessons = findLessonsByCourseId(id);
        course.get().setLessons(lessons);

        return course;
    }

    private List<Lesson> findLessonsByCourseId(Long id) {
        return db.findMany(
                """
                SELECT lesson_id, title, duration, is_completed
                FROM lessons
                WHERE course_id = ?
                ORDER BY lesson_id
                """,
                rs -> {
                    try {
                        return new Lesson(
                                rs.getLong("lesson_id"),
                                rs.getString("title"),
                                rs.getTime("duration").toLocalTime(),
                                rs.getBoolean("is_completed")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                id
        );
    }

    private Course mapCourseRow(ResultSet rs) {
        try {
            Instructor instructor = null;
            Long instrId = rs.getLong("instructor_id");

            instructor = new Instructor();
            instructor.setInstructorId(instrId);
            instructor.setName(rs.getString("name"));
            instructor.setSurname(rs.getString("surname"));
            instructor.setEmail(rs.getString("email"));
            instructor.setUsername(rs.getString("username"));
            instructor.setProfession(rs.getString("profession"));
            instructor.setAbout(rs.getString("bio"));

            Course course = new Course();
            course.setCourseId(rs.getLong("course_id"));
            course.setCourseName(rs.getString("course_name"));
            course.setCategory(rs.getString("category"));
            course.setSatisfactionFactor(rs.getDouble("satisfaction_factor"));
            course.setReviewNumber(rs.getInt("review_number"));
            course.setDuration(rs.getTime("duration").toLocalTime());
            course.setNumOfLectures(rs.getInt("num_of_lectures"));

            course.setLevel(CourseLevel.valueOf(rs.getString("level")));

            course.setType(Collections.singletonList(CourseType.valueOf(rs.getString("type"))));

            course.setInstructor(instructor);

            return course;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
