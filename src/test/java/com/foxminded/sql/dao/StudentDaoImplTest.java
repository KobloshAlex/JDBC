package com.foxminded.sql.dao;

import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.generator.SQLExecutor;
import com.foxminded.sql.DAO.impl.StudentDaoImpl;
import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentDaoImplTest {
    private final DBConnector dbConnector = new DBConnector("db");
    private final StudentDao studentDao = new StudentDaoImpl(dbConnector);
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private final String createTable = Objects.requireNonNull(classLoader.getResource("create_tables.sql")).getFile();
    private final SQLExecutor sqlExecutor = new SQLExecutor();

    private final Map<Student, List<Course>> studentsCourse = new HashMap<>();
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    @BeforeEach
    void setUp() {
        sqlExecutor.executeScript(createTable, dbConnector);

        studentsCourse.put(Student.builder().withId(1).withGroupId(1).withFirstName("test").withLastName("test").build(), courses);
        studentDao.save(students);
    }

    @Test
    void fillCoursesWithStudentsShouldFillCourseWithStudents() {
        studentDao.fillCoursesWithStudents(studentsCourse);
        assertThat(studentsCourse.size(), is(1));
    }

    @Test
    void findByCourseNameShouldReturnListOfStudentsAssignedToCourse() {
        studentDao.fillCoursesWithStudents(studentsCourse);
        assertEquals(students, studentDao.findByCourseName("ESL"));
    }

    @Test
    void assignToCourseShouldAddStudentToCourse() {
        studentDao.assignToCourse(1, 223);
        assertFalse(studentDao.findByCourseName("ESL").isEmpty());

    }

    @Test
    void deleteFromCourseShouldRemoveStudentFromCourse() {
        students.add(Student.builder().withId(2).withGroupId(2).withFirstName("Ron").withLastName("OO").build());
        studentDao.save(students);
        studentDao.assignToCourse(2, 223);
        studentDao.deleteFromCourse(2, 223);
        assertTrue(studentDao.findByCourseName("ESl").isEmpty());
    }
}
