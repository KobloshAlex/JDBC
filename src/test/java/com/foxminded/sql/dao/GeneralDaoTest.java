package com.foxminded.sql.dao;

import com.foxminded.sql.DAO.CourseDao;
import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.GroupDao;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.DAO.impl.CourseDaoImpl;
import com.foxminded.sql.generator.SQLExecutor;
import com.foxminded.sql.DAO.impl.GroupDaoImpl;
import com.foxminded.sql.DAO.impl.StudentDaoImpl;
import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Group;
import com.foxminded.sql.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GeneralDaoTest {
    private final DBConnector dbConnector = new DBConnector("db");
    private final CourseDao courseDao = new CourseDaoImpl(dbConnector);
    private final StudentDao studentDao = new StudentDaoImpl(dbConnector);
    private final GroupDao groupDao = new GroupDaoImpl(dbConnector);
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private final String createUserDbScript = Objects.requireNonNull(classLoader.getResource("create_tables.sql")).getFile();
    private final SQLExecutor sqlExecutor = new SQLExecutor();

    private List<Course> courses = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    @BeforeEach
    void createDataBase() {
        sqlExecutor.executeScript(createUserDbScript, dbConnector);

        courses.add(Course.builder().withId(1).withName("test").withDescription("test").build());
        students.add(Student.builder().withId(1).withGroupId(2).withFirstName("Test").withLastName("Test").build());
        groups.add(Group.builder().withId(1).withName("Test Group").build());
    }

    @Test
    void saveShouldSaveListOfEntitiesToDataBase() {
        courseDao.save(courses);
        assertThat(courses.size(), is(1));
        studentDao.save(students);
        assertThat(students.size(), is(1));
        groupDao.save(groups);
        assertThat(groups.size(), is(1));
    }

    @Test
    void findAllShouldReturnListOfEntitiesFromDataBase() {
        assertThat(courseDao.findAll(1, 1).size(), is(1));
        assertThat(studentDao.findAll(1, 1).size(), is(1));
        assertThat(groupDao.findAll(1, 1).size(), is(1));
    }

    @Test
    void deleteByIdShouldDeleteEntityFromDataBaseById() {
        courseDao.deleteById(1);
        assertTrue(courseDao.findAll(1, 1).isEmpty());
        studentDao.deleteById(1);
        assertTrue(studentDao.findAll(1, 1).isEmpty());
        groupDao.deleteById(1);
        assertTrue(groupDao.findAll(1, 1).isEmpty());
    }
}
