package config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static config.SystemConstants.*;

public class DatabaseConfig {

    public Connection connectionToDatabase() {

        try {
            Class.forName(MYSQL_DRIVER);
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {

        try {
            connectionToDatabase().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
