package com.foxminded.sql.DAO.impl;

import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDaoImpl extends AbstractGeneralDaoImpl<Student> implements StudentDao {
    private static final Logger LOGGER = LogManager.getLogger(StudentDaoImpl.class);

    private static final String SELECT_ALL_QUERY = "SELECT * FROM students OFFSET ? LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE student_id = ?";
    private static final String GET_BY_COURSE_NAME_QUERY = "SELECT students.student_id, students.group_id, students.first_name, students.last_name " +
            "FROM students_courses " +
            "INNER  JOIN students " +
            "ON students_courses.student_id = students.student_id " +
            "INNER  JOIN courses " +
            "ON students_courses.course_id = courses.course_id " +
            "WHERE courses.course_name = ?";
    private static final String ASSIGN_TO_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
    private static final String DELETE_FROM_COURSE_QUERY =
            "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT students.student_id, students.group_id, students.first_name, students.last_name" +
            "FROM students " +
            "WHERE students.student_id = ?";

    private static final String UNABLE_TO_FIND_BY_COURSE_NAME = "unable to return list of students by the course name ";
    private static final String UNABLE_TO_INSERT = "unable to fill table courses with students ";
    private static final String UNABLE_TO_ASSIGN = "unable to assign new student to course ";
    private static final String UNABLE_TO_DELETE = "unable to delete student from course ";

    public StudentDaoImpl(DBConnector dbConnector) {
        super(dbConnector, INSERT_QUERY, SELECT_ALL_QUERY, SELECT_BY_STUDENT_ID_QUERY, DELETE_QUERY);
    }

    @Override
    protected List<Student> processResultSetToList(ResultSet resultSet) throws SQLException {
        List<Student> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(Student.builder()
                    .withId(resultSet.getInt(1))
                    .withGroupId(resultSet.getInt(2))
                    .withFirstName(resultSet.getString(3))
                    .withLastName(resultSet.getString(4))
                    .build());
        }
        return result;
    }

    @Override
    protected void insert(PreparedStatement statement, List<Student> students) throws SQLException {
        for (Student student : students) {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.addBatch();
        }
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> result = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_COURSE_NAME_QUERY)) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processResultSetToList(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_FIND_BY_COURSE_NAME, e);
        }
        return result;
    }

    @Override
    public void fillCoursesWithStudents(Map<Student, List<Course>> map) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_COURSE_QUERY)) {
            for (Map.Entry<Student, List<Course>> entry : map.entrySet()) {
                Student student = entry.getKey();
                for (Course course : entry.getValue()) {
                    statement.setInt(1, student.getId());
                    statement.setInt(2, course.getId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_INSERT, e);
        }
    }

    @Override
    public void assignToCourse(int studentId, int courseId) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_COURSE_QUERY)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_ASSIGN, e);
        }
    }

    @Override
    public void deleteFromCourse(int studentId, int courseId) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FROM_COURSE_QUERY)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_DELETE, e);
        }
    }
}
