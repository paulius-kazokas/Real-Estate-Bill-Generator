package config;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.SystemConstants.*;

@Slf4j
public class DatabaseConfig {

    public Connection connectionToDatabase() throws SQLException {
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }

    public ResultSet resultSet(String query) throws SQLException {
        return !connectionToDatabase().isClosed() && !query.isBlank() ? connectionToDatabase().prepareStatement(query).executeQuery(query) : null;
    }

    public ResultSet resultSet(String query, String queryParam) throws SQLException {
        return !connectionToDatabase().isClosed() && !queryParam.isBlank() || !query.isBlank() ? connectionToDatabase().prepareStatement(query).executeQuery(query) : null;
    }
}
