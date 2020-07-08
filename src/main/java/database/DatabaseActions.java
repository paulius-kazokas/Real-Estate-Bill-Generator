package database;

import config.DatabaseConfig;
import config.SystemConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import utility.InputVadility;

import java.sql.*;

public class DatabaseActions {

    DatabaseConfig databaseConfig;

    public DatabaseActions(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    public String getUserByUsername(String usernameInput) throws SQLException {

        String username = null;

        if (StringUtils.isBlank(usernameInput)) {
            throw new IllegalArgumentException("Username cannot be empty or null");
        }

        String query = "SELECT username FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + usernameInput + "'";
        Statement st = databaseConfig.connectionToDatabase().createStatement();

        try (st; ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                username = rs.getString("username");
            }
        }
        st.close();
        databaseConfig.connectionToDatabase().close();

        return username;
    }

    public boolean checkIfPasswordMatches(String usernameWithThisPassword, String userPasswordInput) throws SQLException {

        String passwordFromDatabase = null;

        if (StringUtils.isBlank(usernameWithThisPassword)) {
            throw new IllegalArgumentException("Username cannot be empty or null");
        }
        if (StringUtils.isBlank(userPasswordInput)) {
            throw new IllegalArgumentException("Password cannot be empty or null");
        }

        String query = "SELECT password FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username =" + usernameWithThisPassword;
        Statement st = databaseConfig.connectionToDatabase().createStatement();

        try (st; ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                passwordFromDatabase = rs.getString("password");
            }
        }
        st.close();
        databaseConfig.connectionToDatabase().close();

        return userPasswordInput.equals(passwordFromDatabase);
    }

    public void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode) throws SQLException {

        String query = "INSERT INTO " + SystemConstants.UTC_USERS_TABLE + " (id, username, password, name, lastname, email, personalcode) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = databaseConfig.connectionToDatabase().prepareStatement(query);
        preparedStmt.setString(1, null);
        preparedStmt.setString(2, regUsername);
        preparedStmt.setString(3, regPassword);
        preparedStmt.setString(4, regName);
        preparedStmt.setString(5, regLastName);
        preparedStmt.setString(6, regEmail);
        preparedStmt.setString(7, regPersonalCode);

        preparedStmt.execute();
        databaseConfig.connectionToDatabase().close();
    }

    public void deleteUserByUsername(String deleteUsername) throws SQLException {

        String query = "DELETE FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + deleteUsername + "'";
        Statement st = databaseConfig.connectionToDatabase().createStatement();
        st.executeUpdate(query);
        st.close();
    }

    // utilities --

    public void queryHeatBill(String username) {

    }
}
