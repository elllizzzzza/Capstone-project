package com.educationalSystem;

import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import com.educationalSystem.repository.CourseRepository;
import com.educationalSystem.repository.LessonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class JpaLifecycleTest {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private LessonRepository lessonRepo;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlatformTransactionManager txManager;

    private <T> T inTx(TxCallback<T> action) {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        return tt.execute(status -> {
            try { return action.execute(); }
            catch (RuntimeException e) { status.setRollbackOnly(); throw e; }
        });
    }

    @FunctionalInterface
    interface TxCallback<T> { T execute(); }

    @AfterEach
    void cleanDb() {
        inTx(() -> {
            em.createQuery("DELETE FROM Lesson l").executeUpdate();
            em.createQuery("DELETE FROM Course c").executeUpdate();
            return null;
        });
    }

    @Test
    void test3_saveParentWithoutId_repository() {
        Course course = new Course(null, "Java Basics", "Programming",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);

        Course saved = courseRepo.save(course);

        assertThat(saved.getCourseId()).isNotNull();
        assertThat(courseRepo.existsById(saved.getCourseId())).isTrue();
    }

    @Test
    void test3_saveParentWithoutId_persist() {
        Course course = new Course(null, "Java Basics", "Programming",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);

        inTx(() -> { em.persist(course); return null; });

        assertThat(course.getCourseId()).isNotNull();
        assertThat(courseRepo.existsById(course.getCourseId())).isTrue();
    }

    @Test
    void test3_saveParentWithoutId_merge() {
        Course course = new Course(null, "Java Basics", "Programming",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);

        Course managed = inTx(() -> em.merge(course));

        assertThat(course.getCourseId()).isNull();
        assertThat(managed.getCourseId()).isNotNull();
        assertThat(courseRepo.existsById(managed.getCourseId())).isTrue();
    }

    @Test
    void test4_saveParentWithId_notInDb_repository() {
        Course course = new Course(999L, "Algorithms", "CS",
                CourseType.AUDIO_BASED, CourseLevel.ADVANCED, null, null, null);

        assertThatThrownBy(() -> courseRepo.save(course))
                .isInstanceOf(Exception.class);
    }

    @Test
    void test4_saveParentWithId_notInDb_merge() {
        Course course = new Course(999L, "Algorithms", "CS",
                CourseType.AUDIO_BASED, CourseLevel.ADVANCED, null, null, null);

        assertThatThrownBy(() -> inTx(() -> em.merge(course)))
                .isInstanceOf(Exception.class);
    }

    private Course persistCourseInDb(String name) {
        return inTx(() -> {
            Course c = new Course(null, name, "Cat",
                    CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
            em.persist(c);
            return c;
        });
    }


    @Test
    void test5_saveParentSameId_repository() {
        Course existing = persistCourseInDb("Original Name");
        Long id = existing.getCourseId();

        Course duplicate = new Course(id, "Updated Name", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        courseRepo.save(duplicate);

        assertThat(courseRepo.findById(id))
                .isPresent()
                .get()
                .extracting(Course::getCourseName)
                .isEqualTo("Updated Name");
    }

    @Test
    void test5_saveParentSameId_persist() {
        Course existing = persistCourseInDb("Original Name");
        Long id = existing.getCourseId();

        Course duplicate = new Course(id, "Conflicting Name", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);

        assertThatThrownBy(() ->
                inTx(() -> { em.persist(duplicate); return null; })
        ).isInstanceOf(Exception.class);
    }


    @Test
    void test5_saveParentSameId_merge() {
        Course existing = persistCourseInDb("Original Name");
        Long id = existing.getCourseId();

        Course duplicate = new Course(id, "Merged Name", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        inTx(() -> em.merge(duplicate));

        assertThat(courseRepo.findById(id))
                .isPresent()
                .get()
                .extracting(Course::getCourseName)
                .isEqualTo("Merged Name");
    }

    private Course buildCourseWithLessons(Long courseId, String courseName) {
        Course c = new Course(courseId, courseName, "Programming",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        Lesson l1 = new Lesson(null, "Intro", 30, c);
        Lesson l2 = new Lesson(null, "Loops", 45, c);
        c.setLessons(List.of(l1, l2));
        return c;
    }


    @Test
    void test6_saveParentWithNewChildren_repository() {
        Course course = buildCourseWithLessons(null, "Spring Boot");

        Course saved = courseRepo.save(course);

        assertThat(saved.getCourseId()).isNotNull();
        assertThat(lessonRepo.count()).isEqualTo(2);
    }


    @Test
    void test6_saveParentWithNewChildren_persist() {
        Course course = buildCourseWithLessons(null, "Spring Boot");

        inTx(() -> { em.persist(course); return null; });

        assertThat(course.getCourseId()).isNotNull();
        assertThat(lessonRepo.count()).isEqualTo(2);
    }

    @Test
    void test6_saveParentWithNewChildren_merge() {
        Course course = buildCourseWithLessons(null, "Spring Boot");

        Course managed = inTx(() -> em.merge(course));

        assertThat(managed.getCourseId()).isNotNull();
        assertThat(lessonRepo.count()).isEqualTo(2);
    }

    @Test
    void test7_saveParentWithExistingChildren_repository() {
        Course saved = inTx(() -> {
            Course c = buildCourseWithLessons(null, "Kotlin Basics");
            em.persist(c);
            return c;
        });

        Course detached = courseRepo.findById(saved.getCourseId()).orElseThrow();
        detached.setCourseName("Kotlin Advanced");

        courseRepo.save(detached);

        assertThat(courseRepo.findById(saved.getCourseId()))
                .isPresent()
                .get()
                .extracting(Course::getCourseName)
                .isEqualTo("Kotlin Advanced");
        assertThat(lessonRepo.count()).isEqualTo(2);
    }


    @Test
    void test7_saveParentWithExistingChildren_merge() {
        Course saved = inTx(() -> {
            Course c = buildCourseWithLessons(null, "Kotlin Basics");
            em.persist(c);
            return c;
        });

        Course detached = courseRepo.findById(saved.getCourseId()).orElseThrow();
        detached.setCourseName("Kotlin Expert");

        inTx(() -> em.merge(detached));

        assertThat(courseRepo.findById(saved.getCourseId()))
                .isPresent()
                .get()
                .extracting(Course::getCourseName)
                .isEqualTo("Kotlin Expert");
    }


    @Test
    void test8_saveChildWithoutParent_repository() {
        Lesson lesson = new Lesson(null, "Orphan Lesson", 20, null);

        Lesson saved = lessonRepo.save(lesson);

        assertThat(saved.getLessonId()).isNotNull();
        assertThat(saved.getCourse()).isNull();
    }

    @Test
    void test8_saveChildWithoutParent_persist() {
        Lesson lesson = new Lesson(null, "Orphan Lesson", 20, null);

        inTx(() -> { em.persist(lesson); return null; });

        assertThat(lesson.getLessonId()).isNotNull();
    }

    @Test
    void test8_saveChildWithoutParent_merge() {
        Lesson lesson = new Lesson(null, "Orphan Lesson", 20, null);

        Lesson managed = inTx(() -> em.merge(lesson));

        assertThat(managed.getLessonId()).isNotNull();
    }

    @Test
    void test9_saveChildWithTransientParent_repository() {
        Course transientCourse = new Course(null, "Ghost Course", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        Lesson lesson = new Lesson(null, "Orphan Lesson", 20, transientCourse);

        assertThatThrownBy(() -> lessonRepo.save(lesson))
                .isInstanceOf(Exception.class);
    }


    @Test
    void test9_saveChildWithTransientParent_persist() {
        Course transientCourse = new Course(null, "Ghost Course", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        Lesson lesson = new Lesson(null, "Lesson", 20, transientCourse);

        assertThatThrownBy(() ->
                inTx(() -> { em.persist(lesson); return null; })
        ).isInstanceOf(Exception.class);
    }

    @Test
    void test9_saveChildWithTransientParent_merge() {
        Course transientCourse = new Course(null, "Ghost Course", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
        Lesson lesson = new Lesson(null, "Lesson", 20, transientCourse);

        assertThatThrownBy(() ->
                inTx(() -> em.merge(lesson))
        ).isInstanceOf(Exception.class);
    }


    @Test
    void test10_saveChildWithDetachedParent_repository() {
        Course persistedCourse = inTx(() -> {
            Course c = new Course(null, "Existing Course", "Cat",
                    CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
            em.persist(c);
            return c;
        });

        Lesson lesson = new Lesson(null, "New Lesson", 60, persistedCourse);
        Lesson saved = lessonRepo.save(lesson);

        assertThat(saved.getLessonId()).isNotNull();
        Optional<Lesson> fetched = lessonRepo.findById(saved.getLessonId());
        assertThat(fetched).isPresent();
        Long courseId = inTx(() -> {
            Lesson l = em.find(Lesson.class, saved.getLessonId());
            return l.getCourse() != null ? l.getCourse().getCourseId() : null;
        });
        assertThat(courseId).isEqualTo(persistedCourse.getCourseId());
    }


    @Test
    void test10_saveChildWithDetachedParent_persist() {
        Course persistedCourse = inTx(() -> {
            Course c = new Course(null, "Existing Course", "Cat",
                    CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
            em.persist(c);
            return c;
        });

        Lesson lesson = new Lesson(null, "New Lesson", 60, persistedCourse);

        assertThatCode(() ->
                inTx(() -> { em.persist(lesson); return null; })
        ).doesNotThrowAnyException();

        assertThat(lesson.getLessonId()).isNotNull();
        Long storedCourseId = inTx(() -> {
            Lesson l = em.find(Lesson.class, lesson.getLessonId());
            return l.getCourse().getCourseId();
        });
        assertThat(storedCourseId).isEqualTo(persistedCourse.getCourseId());
    }

    @Test
    void test10_saveChildWithDetachedParent_merge() {
        Course persistedCourse = inTx(() -> {
            Course c = new Course(null, "Existing Course", "Cat",
                    CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null);
            em.persist(c);
            return c;
        });

        Lesson lesson = new Lesson(null, "New Lesson", 60, persistedCourse);

        Lesson managed = inTx(() -> em.merge(lesson));

        assertThat(managed.getLessonId()).isNotNull();
        Long courseId = inTx(() -> {
            Lesson l = em.find(Lesson.class, managed.getLessonId());
            return l.getCourse() != null ? l.getCourse().getCourseId() : null;
        });
        assertThat(courseId).isEqualTo(persistedCourse.getCourseId());
    }


    @Test
    void test11_modifyDetachedEntityWithoutTransaction() {
        Course saved = courseRepo.save(new Course(null, "Original", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null));

        Course detached = courseRepo.findById(saved.getCourseId()).orElseThrow();
        detached.setCourseName("Modified");

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(jakarta.persistence.TransactionRequiredException.class);

        String dbName = courseRepo.findById(saved.getCourseId())
                .map(Course::getCourseName).orElse(null);
        assertThat(dbName).isEqualTo("Original");
    }

    @Test
    void test12_modifyManagedEntityInsideTransaction_dirtyChecking() {
        Course saved = courseRepo.save(new Course(null, "Original", "Cat",
                CourseType.AUDIO_BASED, CourseLevel.BEGINNER, null, null, null));

        inTx(() -> {
            Course managed = em.find(Course.class, saved.getCourseId());
            managed.setCourseName("AutoUpdated");
            em.flush();
            return null;
        });

        String dbName = courseRepo.findById(saved.getCourseId())
                .map(Course::getCourseName).orElse(null);
        assertThat(dbName).isEqualTo("AutoUpdated");
    }
}