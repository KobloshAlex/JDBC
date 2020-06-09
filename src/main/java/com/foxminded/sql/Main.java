package com.foxminded.sql;

import com.foxminded.sql.DAO.impl.CourseDaoImpl;
import com.foxminded.sql.generator.GeneratorTestData;
import com.foxminded.sql.generator.SQLExecutor;
import com.foxminded.sql.controller.Controller;
import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.impl.GroupDaoImpl;
import com.foxminded.sql.DAO.CourseDao;
import com.foxminded.sql.DAO.GroupDao;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.DAO.impl.StudentDaoImpl;
import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Group;
import com.foxminded.sql.domain.Student;
import com.foxminded.sql.view.ConsoleViewProvider;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {

    private static final DBConnector DB_CONNECTOR = new DBConnector("db");
    private static final DBConnector SUPER_ADMIN = new DBConnector("super_admin");

    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    private static final String CREATE_USER_DB_SCRIPT = Objects.requireNonNull(CLASS_LOADER.getResource("create_user_db.sql")).getFile();
    private static final String CREATE_TABLES_SCRIPT = Objects.requireNonNull(CLASS_LOADER.getResource("create_tables.sql")).getFile();
    private static final String DROP_USER_DB_SCRIPT = Objects.requireNonNull(CLASS_LOADER.getResource("drop_user_db.sql")).getFile();

    private static final GeneratorTestData GENERATOR_TEST_DATA = new GeneratorTestData();
    private static final SQLExecutor SCRIPT_EXECUTOR = new SQLExecutor();

    private static final List<Group> GROUPS = GENERATOR_TEST_DATA.getGroups();
    private static final List<Course> COURSES = GENERATOR_TEST_DATA.getCourses();
    private static final List<Student> STUDENTS = GENERATOR_TEST_DATA.getStudents(GROUPS);

    private static final Map<Student, List<Course>> STUDENTS_COURSE = GENERATOR_TEST_DATA.getStudentsCourses(STUDENTS, COURSES);

    private static final CourseDao COURSE_DAO = new CourseDaoImpl(DB_CONNECTOR);
    private static final StudentDao STUDENT_DAO = new StudentDaoImpl(DB_CONNECTOR);
    private static final GroupDao GROUP_DAO = new GroupDaoImpl(DB_CONNECTOR);

    private static final ConsoleViewProvider CONSOLE_VIEW_PROVIDER = new ConsoleViewProvider();
    private static final Controller CONTROLLER = new Controller(COURSE_DAO, GROUP_DAO, STUDENT_DAO, CONSOLE_VIEW_PROVIDER);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        SCRIPT_EXECUTOR.executeScript(CREATE_USER_DB_SCRIPT, SUPER_ADMIN);
        SCRIPT_EXECUTOR.executeScript(CREATE_TABLES_SCRIPT, DB_CONNECTOR);

        GROUP_DAO.save(GROUPS);
        COURSE_DAO.save(COURSES);
        STUDENT_DAO.save(STUDENTS);

        STUDENT_DAO.fillCoursesWithStudents(STUDENTS_COURSE);

        CONTROLLER.callMenu();

        SCRIPT_EXECUTOR.executeScript(DROP_USER_DB_SCRIPT, SUPER_ADMIN);
    }
}
