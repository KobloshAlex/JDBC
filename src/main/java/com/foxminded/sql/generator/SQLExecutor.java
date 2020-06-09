package com.foxminded.sql.generator;

import com.foxminded.sql.DAO.DBConnector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class SQLExecutor {
    private static final Logger LOGGER = LogManager.getLogger(SQLExecutor.class);

    private static final String UNABLE_TO_RETRIEVE_SQL_SCRIPT = "unable to retrieve SQL script ";
    private static final String UNABLE_TO_CONNECT_TO_DATABASE = "unable to connect to database ";
    private static final String REGEX = ";";

    public void executeScript(String scriptPath, DBConnector dbConnector) {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement();
             FileReader fileReader = new FileReader(scriptPath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String[] scriptLines = stringBuilder.toString().split(REGEX);

            Arrays.stream(scriptLines).forEach(scripts -> {
                if (!scripts.trim().equals("")) {
                    try {
                        statement.executeUpdate(scripts);
                    } catch (SQLException e) {
                        LOGGER.error(UNABLE_TO_RETRIEVE_SQL_SCRIPT + e);
                    }
                }
            });

        } catch (IOException e) {
            LOGGER.error(UNABLE_TO_RETRIEVE_SQL_SCRIPT + e);
        } catch (SQLException e) {
            LOGGER.error(UNABLE_TO_CONNECT_TO_DATABASE + e);
        }
    }
}
