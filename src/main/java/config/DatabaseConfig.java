package config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

import static config.SystemConstants.*;

public class DatabaseConfig {

    @SneakyThrows
    public Connection connectionToDatabase() {

        Class.forName(MYSQL_DRIVER);
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }

    @SneakyThrows
    public void closeConnection() {
        connectionToDatabase().close();
    }

}
