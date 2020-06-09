package com.foxminded.sql.DAO.impl;

import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.GroupDao;
import com.foxminded.sql.domain.Group;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl extends AbstractGeneralDaoImpl<Group> implements GroupDao {
    private static final Logger LOGGER = LogManager.getLogger(GroupDaoImpl.class);

    private static final String SELECT_ALL_QUERY = "SELECT * FROM groups OFFSET ? LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO groups (group_id, group_name) VALUES (?,?)";
    private static final String GET_BY_STUDENTS_COUNT_QUERY = "SELECT groups.group_name, COUNT(students.student_id) " +
            "FROM groups " +
            "LEFT JOIN students " +
            "ON students.group_id = groups.group_id " +
            "GROUP BY groups.group_id " +
            "HAVING COUNT(*) <= ?";
    private static final String SELECT_BY_GROUP_ID_QUERY = "SELECT groups.group_id, groups.group_name " +
            "FROM groups " +
            "WHERE groups.group_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE group_id = ?";

    private static final String UNABLE_TO_GET_STUDENT_ID = "unable to return group by student ID ";



    public GroupDaoImpl(DBConnector dbConnector) {
        super(dbConnector, INSERT_QUERY, SELECT_ALL_QUERY, SELECT_BY_GROUP_ID_QUERY, DELETE_QUERY);

    }

    @Override
    protected List<Group> processResultSetToList(ResultSet resultSet) throws SQLException {
        List<Group> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(Group.builder()
                    .withId(resultSet.getInt(1))
                    .withName(resultSet.getString(2))
                    .build());
        }
        return result;
    }

    @Override
    protected void insert(PreparedStatement statement, List<Group> groups) throws SQLException {
        for (Group group : groups) {
            statement.setInt(1, group.getId());
            statement.setString(2, group.getName());
            statement.addBatch();
        }
    }

    @Override
    public List<Group> getByStudentCount(int count) {
        List<Group> result = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_STUDENTS_COUNT_QUERY)) {
            statement.setInt(1, count);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(Group.builder()
                            .withId(resultSet.getInt(2))
                            .withName(resultSet.getString(1))
                            .build());
                }
            }
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_GET_STUDENT_ID, e);
        }
        return result;
    }
}
