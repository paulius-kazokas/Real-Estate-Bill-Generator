package database;

import config.DatabaseConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import utility.InputVadility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseActions {

    DatabaseConfig databaseConfig;

    public DatabaseActions(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    public String checkIfUserExists(String usernameInput) throws SQLException {

        String username = null;

        if (StringUtils.isBlank(usernameInput)) {
            throw new IllegalArgumentException("Username cannot be empty or null");
        }

        String query = "SELECT username FROM utc.users WHERE username = '" + usernameInput + "'";
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

        String query = "SELECT password FROM utc.users WHERE username =" + usernameWithThisPassword;
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

        InputVadility iv = new InputVadility();

        if (iv.checkArrayForFalseValue(ArrayUtils.toArray(regUsername, regPassword, regName, regLastName, regEmail, regPersonalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        String query = "INSERT INTO utc.users (id, username, password, name, lastname, email, personalcode) VALUES (?, ?, ?, ?, ?, ?, ?)";

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

    public int deleteUserByUsername(String deleteUsername) throws SQLException {

        String query = "DELETE FROM utc.users WHERE username = '" + deleteUsername + "'";
        Statement st = databaseConfig.connectionToDatabase().createStatement();
        int rs = st.executeUpdate(query);
        st.close();
        return rs;
    }

    // utilities --

    public void queryHeatBill(String username) {

    }
}
