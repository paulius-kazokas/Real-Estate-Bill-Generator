package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static config.SystemConstants.*;

public class DatabaseConfig {

    public Connection connectionToDatabase() {

        try {
            Class.forName(MYSQL_DRIVER);
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

}
