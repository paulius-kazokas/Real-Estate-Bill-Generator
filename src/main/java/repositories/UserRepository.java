package repositories;

import config.DatabaseConfig;
import config.SystemConstants;
import entities.User;
import interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.SecurityUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements IUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private DatabaseConfig databaseConfig;

    public UserRepository(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    @Override
    public boolean checkIfUserExists(String username) {

        String query = "SELECT username FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement st = databaseConfig.connectionToDatabase().createStatement(); ResultSet rs = st.executeQuery(query)) {
            return rs.next() && !rs.getString("username").isBlank();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return false;
    }

    @Override
    public User getUserByUsername(String username) {

        String query = "SELECT * FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement st = databaseConfig.connectionToDatabase().createStatement(); ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setPersonalCode(rs.getString("personalcode"));

                return user;
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
            LOGGER.error(String.format("%s", e));
        }
    }

    @Override
    public void deleteUserByUsername(String username) {

        String query = "DELETE FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement()) {
            statement.executeUpdate(query);
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }
    }

    @Override
    public String getPersonalCodeByUsername(String username) {

        String query = "SELECT personalcode FROM " + SystemConstants.UTC_USERS_TABLE + " WHERE username = '" + username + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                String personalCode = resultSet.getString("personalcode");
                if (!personalCode.isBlank()) return personalCode;
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

}
