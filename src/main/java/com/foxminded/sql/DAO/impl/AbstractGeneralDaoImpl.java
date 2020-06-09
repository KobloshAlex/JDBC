package com.foxminded.sql.DAO.impl;

import com.foxminded.sql.DAO.DBConnector;
import com.foxminded.sql.DAO.GeneralDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGeneralDaoImpl<E> implements GeneralDao<E, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(AbstractGeneralDaoImpl.class);

    private static final String UNABLE_TO_INSERT = "unable to insert TestData into table ";
    private static final String UNABLE_TO_FIND_ALL = "unable to return find all query ";
    private static final String UNABLE_TO_GET_BY_ID = "unable to get by id ";
    private static final String UNABLE_TO_DELETE = "Unable to delete record";

    protected final DBConnector dbConnector;
    private final String insertQuery;
    private final String findAllQuery;
    private final String getByIdQuery;
    private final String deleteByIdQuery;

    protected AbstractGeneralDaoImpl(DBConnector dbConnector, String insertQuery, String findAllQuery, String getByIdQuery, String deleteByIdQuery) {
        this.dbConnector = dbConnector;
        this.insertQuery = insertQuery;
        this.findAllQuery = findAllQuery;
        this.getByIdQuery = getByIdQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(List<E> entities) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            insert(statement, entities);
            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_INSERT, e);
        }
    }

    @Override
    public List<E> findAll(Integer offset, Integer limit) {
        List<E> result = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)){
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                result = processResultSetToList(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_FIND_ALL, e);
        }
        return result;
    }

    @Override

    public List<E> findById(Integer id) {
        List<E> result = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getByIdQuery)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = processResultSetToList(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_GET_BY_ID, e);
        }
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_DELETE, e);
        }
    }

    protected abstract List<E> processResultSetToList(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement statement, List<E> entities) throws SQLException;
}
