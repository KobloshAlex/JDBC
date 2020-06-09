package com.foxminded.sql.DAO;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnector {
    private static final String DB_URL = "url";
    private static final String ADMIN = "admin";
    private static final String PASS = "pass";
    private static final String MIN_IDLE = "minIdle";
    private static final String MAX_IDLE = "maxIdle";
    private static final String MAX_OPEN_STATEMENTS = "maxOpenStatements";

    private final BasicDataSource dataSource = new BasicDataSource();

    private final String url;
    private final String user;
    private final String password;
    private final Integer setMinIdle;
    private final Integer setMaxIdle;
    private final Integer setMaxOpenPreparedStatements;

    public DBConnector(String filePropertiesName) {
        ResourceBundle resources = ResourceBundle.getBundle(filePropertiesName);
        this.url = resources.getString(DB_URL);
        this.user = resources.getString(ADMIN);
        this.password = resources.getString(PASS);
        this.setMinIdle = Integer.parseInt(resources.getString(MIN_IDLE));
        this.setMaxIdle = Integer.parseInt(resources.getString(MAX_IDLE)) ;
        this.setMaxOpenPreparedStatements = Integer.parseInt(resources.getString(MAX_OPEN_STATEMENTS));
    }

    public Connection getConnection() throws SQLException {
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMinIdle(setMinIdle);
        dataSource.setMaxIdle(setMaxIdle);
        dataSource.setMaxOpenPreparedStatements(setMaxOpenPreparedStatements);
        return dataSource.getConnection();
    }
}
