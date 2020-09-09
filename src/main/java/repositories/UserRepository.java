package repositories;

import config.DatabaseConfig;
import entities.User;
import interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.SecurityUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static config.SystemConstants.*;

public class UserRepository implements IUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private DatabaseConfig databaseConfig;

    public UserRepository(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    @Override
    public Integer getUserId(String username) {

        if (!username.isBlank()) {
            if (checkIfUserExists(username)) {

                String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                        UTC_USERS_TABLE_ID,
                        UTC_USERS_TABLE,
                        UTC_USERS_TABLE_USERNAME, username);

                try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {
                    if (resultSet.next()) return resultSet.getInt(UTC_USERS_TABLE_ID);
                } catch (SQLException e) {
                    LOGGER.error(e.toString());
                }
            } else {
                LOGGER.warn("'{}' does not exist", username);
            }
        } else {
            LOGGER.error("Invalid username");
        }

        return null;
    }

    @Override
    public boolean checkIfUserExists(String username) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                UTC_USERS_TABLE_USERNAME,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, username);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                String receivedUsername = resultSet.getString(UTC_USERS_TABLE_USERNAME);
                if (!receivedUsername.isBlank() && receivedUsername.equals(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return false;
    }

    @Override
    public User getUserByUsername(String username) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                SELECT_ALL,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, username);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return User.builder()
                        .id(getUserId(username))
                        .username(username)
                        .password(resultSet.getString(UTC_USERS_TABLE_PASSWORD))
                        .name(resultSet.getString(UTC_USERS_TABLE_NAME))
                        .lastname(resultSet.getString(UTC_USERS_TABLE_LASTNAME))
                        .email(resultSet.getString(UTC_USERS_TABLE_EMAIL))
                        .personalCode(resultSet.getString(UTC_USERS_TABLE_PERSONAL_CODE))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public User getUser(int personalCode) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                SELECT_ALL,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_PERSONAL_CODE, personalCode);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt(UTC_USERS_TABLE_ID))
                        .personalCode(resultSet.getString(UTC_USERS_TABLE_PERSONAL_CODE))
                        .username(resultSet.getString(UTC_USERS_TABLE_USERNAME))
                        .password(resultSet.getString(UTC_USERS_TABLE_PASSWORD))
                        .name(resultSet.getString(UTC_USERS_TABLE_NAME))
                        .lastname(resultSet.getString(UTC_USERS_TABLE_LASTNAME))
                        .email(resultSet.getString(UTC_USERS_TABLE_EMAIL))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public boolean checkIfPasswordMatches(String password, String userDatabasePassword, SecurityUtils securityUtils) {
        return securityUtils.sha512Hash(password).equals(userDatabasePassword);
    }

    @Override
    public void registerNewUser(String username, String password, String name, String lastname, String email, String personalCode) {

        String query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, UTC_USERS_TABLE_PASSWORD, UTC_USERS_TABLE_NAME, UTC_USERS_TABLE_LASTNAME, UTC_USERS_TABLE_EMAIL, UTC_USERS_TABLE_PERSONAL_CODE);

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
            LOGGER.error(String.format("%s", e));
        }

    }

    @Override
    public void deleteUserByUsername(String username) {

        String query = "DELETE FROM " + UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement()) {
            statement.executeUpdate(query);
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }
    }

    @Override
    public String getPersonalCodeByUsername(String username) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                UTC_USERS_TABLE_PERSONAL_CODE,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, username);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                String personalCode = resultSet.getString(UTC_USERS_TABLE_PERSONAL_CODE);
                if (!personalCode.isBlank()) return personalCode;
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

}
