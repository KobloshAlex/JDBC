package com.foxminded.sql.DAO.impl;

import com.foxminded.sql.DAO.CourseDao;
import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.domain.Course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl extends AbstractGeneralDaoImpl<Course> implements CourseDao {

    private static final String SELECT_ALL_QUERY = "SELECT * FROM courses OFFSET ? LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO courses (course_id, course_name, course_description) VALUES (?, ?, ?)";
    private static final String GET_BY_STUDENT_ID_QUERY =
            "SELECT courses.course_id, courses.course_name, courses.course_description " +
                    "FROM students_courses " +
                    "INNER JOIN courses " +
                    "ON students_courses.course_id = courses.course_id " +
                    "WHERE students_courses.student_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE course_id = ?";

    public CourseDaoImpl(DBConnector dbConnector) {
        super(dbConnector, INSERT_QUERY, SELECT_ALL_QUERY, GET_BY_STUDENT_ID_QUERY, DELETE_QUERY);
    }

    @Override
    protected List<Course> processResultSetToList(ResultSet resultSet) throws SQLException {
        List<Course> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(Course.builder()
                    .withId(resultSet.getInt(1))
                    .withName(resultSet.getString(2))
                    .withDescription(resultSet.getString(3))
                    .build());
        }
        return result;
    }

    @Override
    protected void insert(PreparedStatement statement, List<Course> courses) throws SQLException {
        for (Course course : courses) {
            statement.setInt(1, course.getId());
            statement.setString(2, course.getName());
            statement.setString(3, course.getDescription());
            statement.addBatch();
        }
    }
}
