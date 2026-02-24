package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class CourseSpecification {

    private CourseSpecification() {}

    public static Specification<Course> courseNameContains(String name) {
        return (root, query, cb) ->
                name == null ? null
                        : cb.like(cb.lower(root.get("courseName")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Course> categoryContains(String category) {
        return (root, query, cb) ->
                category == null ? null
                        : cb.like(cb.lower(root.get("category")),
                        "%" + category.toLowerCase() + "%");
    }

    public static Specification<Course> typeIn(List<CourseType> types) {
        return (root, query, cb) ->
                CollectionUtils.isEmpty(types) ? null
                        : root.get("type").in(types);
    }

    public static Specification<Course> levelIn(List<CourseLevel> levels) {
        return (root, query, cb) ->
                CollectionUtils.isEmpty(levels) ? null
                        : root.get("level").in(levels);
    }

    public static Specification<Course> instructorIdEquals(Long instructorId) {
        return (root, query, cb) ->
                instructorId == null ? null
                        : cb.equal(root.get("instructor").get("id"), instructorId);
    }

    public static Specification<Course> fromFilter(CourseFilter filter) {
        return Specification.allOf(
                courseNameContains(filter.getCourseName()),
                courseNameContains(filter.getCourseName()),
                categoryContains(filter.getCategory()),
                typeIn(filter.getType()),
                levelIn(filter.getLevel()),
                instructorIdEquals(filter.getInstructorId())
        );
    }
}