package database;

import java.sql.*;

public class DatabaseActions {

    public String checkIfUserExists(Connection con, String usernameInput) throws SQLException {

        String username = null;
        String query = "SELECT username FROM utc.users WHERE username =" + usernameInput;
        Statement st = con.createStatement();

        try (st; ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                username = rs.getString("username");
            }
        }
        st.close();

        return username;
    }

    public boolean checkIfPasswordMatches(Connection con, String usernameWithThisPassword, String userPasswordInput) throws SQLException {

        String passwordFromDatabase = null;
        String query = "SELECT password FROM utc.users WHERE username =" + usernameWithThisPassword;
        Statement st = con.createStatement();

        try (st; ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                passwordFromDatabase = rs.getString("password");
            }
        }
        st.close();

        return userPasswordInput.equals(passwordFromDatabase);
    }

    public void registerNewUser(Connection con, String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode) throws SQLException {

        String query = "INSERT INTO utc.users (id, username, password, name, lastname, email, asmenskodas) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStmt = con.prepareStatement(query)) {
            preparedStmt.setString(1, null);
            preparedStmt.setString(2, regUsername);
            preparedStmt.setString(3, regPassword);
            preparedStmt.setString(4, regName);
            preparedStmt.setString(5, regLastName);
            preparedStmt.setString(6, regEmail);
            preparedStmt.setString(7, regPersonalCode);

            preparedStmt.execute();
        }
        con.close();
    }
}
