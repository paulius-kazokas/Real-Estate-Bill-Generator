package config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.SystemConstants.*;

@Slf4j
public class DatabaseConfig {

    public Connection connectionToDatabase() {

        try {
            Class.forName(MYSQL_DRIVER);
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e.toString());
        }
        return null;
    }

    @SneakyThrows(IOException.class)
    public ResultSet resultSet(String query, String queryParam) {

        try {
            if (!connectionToDatabase().isClosed()) {
                if (!queryParam.isBlank() || !query.isBlank())
                    return connectionToDatabase().prepareStatement(query).executeQuery(query);
            } else {
                log.error("connection to the database is not esablished");
                return null;
            }
        } catch (SQLException sql) {
            log.error(sql.toString());
        }

        OUT.write(String.format("Invalid parameter detected: query - '%s'; queryParam - '%s'", query, queryParam).getBytes());
        return null;
    }

    @SneakyThrows(IOException.class)
    public ResultSet resultSet(String query, String queryParam1, String queryParam2) {

        try {
            if (!connectionToDatabase().isClosed()) {
                if (!queryParam1.isBlank() || !queryParam2.isBlank() || !query.isBlank())
                    return connectionToDatabase().prepareStatement(query).executeQuery(query);
            } else {
                log.error("connection to the database is not esablished");
                return null;
            }
        } catch (SQLException sql) {
            log.error(sql.toString());
        }

        OUT.write(String.format("Invalid parameter detected: query - '%s'; queryParam1 - '%s'; queryParam2 - '%s'", query, queryParam1, queryParam2).getBytes());
        return null;
    }
}
