package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/utc?useSSL=true";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";

    public Connection connectionToDatabase() {

        Connection connection = null;

        try {
            Class.forName(MYSQL_DRIVER);
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

        if(connection == null) {
            LOGGER.error("Unable to create connection with database..");
        }

        return connection;
    }
}
