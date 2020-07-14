package repositories;

import config.DatabaseConfig;
import config.SystemConstants;
import interfaces.UserInterface;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements UserInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    DatabaseConfig databaseConfig;

    public UserRepository(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    @Override
    public String checkIfUserAlreadyExists(String username) {

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be empty or null");
        }

        String query = "SELECT username FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";
        try (Statement st = databaseConfig.connectionToDatabase().createStatement(); ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("username");
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

    @Override
    public String getInformationByUsername(String username, String criteria) {

        String query = "SELECT " + criteria + " FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";
        try (Statement st = databaseConfig.connectionToDatabase().createStatement(); ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString(criteria);
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public boolean checkIfPasswordMatches(String username, String password) {

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be empty or null");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password cannot be empty or null");
        }

        String query = "SELECT password FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return false;
    }

    @Override
    public void registerNewUser(String username, String password, String name, String lastname, String email, String personalCode) {

        String query = "INSERT INTO " + SystemConstants.UTC_USERS_TABLE + " (username, password, name, lastname, email, personalcode) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = databaseConfig.connectionToDatabase().prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, lastname);
            statement.setString(5, email);
            statement.setString(6, personalCode);

            statement.execute();

            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    @Override
    public void deleteUserByUsername(String username) {

        String query = "DELETE FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    @Override
    public String getPersonalCodeByUsername(String username) {

        String query = "SELECT personalcode FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getString("personalcode");
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

}
