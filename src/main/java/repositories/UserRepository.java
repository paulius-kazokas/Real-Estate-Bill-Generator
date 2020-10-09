package repositories;

import config.DatabaseConfig;
import entities.User;
import interfaces.IUserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import utils.SecurityUtils;
import utils.InputUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static config.SystemConstants.*;

@Slf4j
public class UserRepository implements IUserRepository {

    private static final OutputStream OUT = System.out;
    private DatabaseConfig databaseConfig;

    public UserRepository(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    // TODO: perkelti į Utils clase ir is ten naudoti
    @SneakyThrows
    public ResultSet resultSet(String query, String queryParam) {
        if (!queryParam.isBlank() || !query.isBlank()) {
            return databaseConfig.connectionToDatabase().createStatement().executeQuery(query);
        }
        OUT.write(String.format("Invalid parameter detected: query - '%s'; queryParam - '%s'", query, queryParam).getBytes());
        return null;
    }

    private boolean userValidity(String param) {
        return !param.isBlank() && checkIfUserExists(param);
    }

    @Override
    public Integer getUserId(String username) {

        ResultSet resultSet = resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s'",
                UTC_USERS_TABLE_ID,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, username),
                username);

        try {
            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt(UTC_USERS_TABLE_ID);
                databaseConfig.closeConnection();
                return id;
            }
        } catch (SQLException sql) {
            log.error(sql.toString());
        }

        return null;
    }

    @Override
    public boolean checkIfUserExists(String username) {

        ResultSet resultSet = resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s'",
                UTC_USERS_TABLE_USERNAME,
                UTC_USERS_TABLE,
                UTC_USERS_TABLE_USERNAME, username),
                username);

        try {
            if (resultSet != null && resultSet.next()) {
                String receivedUsername = resultSet.getString(UTC_USERS_TABLE_USERNAME);
                databaseConfig.closeConnection();
                return !receivedUsername.isBlank() && receivedUsername.equals(username);
            }
        } catch (SQLException sql) {
            log.error(sql.toString());
        }

        return false;
    }

    @SneakyThrows(IOException.class)
    @Override
    public User getUserByUsername(String username) {

        if (userValidity(username)) {
            ResultSet resultSet = resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s'", SELECT_ALL, UTC_USERS_TABLE, UTC_USERS_TABLE_USERNAME, username), username);
            try {
                if (resultSet.next()) {
                    // array check
                    Integer userId = getUserId(username);
                    String userPassword = resultSet.getString(UTC_USERS_TABLE_PASSWORD);
                    String userName = resultSet.getString(UTC_USERS_TABLE_NAME);
                    String userLastname = resultSet.getString(UTC_USERS_TABLE_LASTNAME);
                    String userEmail = resultSet.getString(UTC_USERS_TABLE_EMAIL);
                    String userPersonalCode = resultSet.getString(UTC_USERS_TABLE_PERSONAL_CODE);
                    databaseConfig.closeConnection();

                    if (InputUtils.validArray(new String[]{userId.toString(), userPassword, username, userName, userLastname, userEmail, userPersonalCode})) {
                        User user = User.object();
                        user.setId(userId);
                        user.setUsername(username);
                        user.setPassword(userPassword);
                        user.setName(userName);
                        user.setLastname(userLastname);
                        user.setEmail(userEmail);
                        user.setPersonalCode(userPersonalCode);

                        return user;
                    }
                }
            } catch (SQLException sql) {
                log.error(sql.toString());
            }
        }
        OUT.write("Invalid username provided".getBytes());

        return null;
    }

    @Override
    public User getUser(String personalCode) {

        ResultSet resultSet = resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s'", SELECT_ALL, UTC_USERS_TABLE, UTC_USERS_TABLE_PERSONAL_CODE, personalCode), personalCode);

        try {
            if (resultSet.next()) {
                int userId = resultSet.getInt(UTC_USERS_TABLE_ID);
                String userUsername = resultSet.getString(UTC_USERS_TABLE_USERNAME);
                String userPassword = resultSet.getString(UTC_USERS_TABLE_PASSWORD);
                String userName = resultSet.getString(UTC_USERS_TABLE_NAME);
                String userLastname = resultSet.getString(UTC_USERS_TABLE_LASTNAME);
                String userEmail = resultSet.getString(UTC_USERS_TABLE_EMAIL);
                String userPersonalCode = resultSet.getString(UTC_USERS_TABLE_PERSONAL_CODE);
                databaseConfig.closeConnection();

                String[] gatheredData = {Integer.toString(userId), userUsername, userPassword, userName, userLastname, userEmail, userPersonalCode};
                boolean isValidData = InputUtils.validArray(gatheredData);

                if (isValidData) {
                    User user = User.object();
                    user.setId(userId);
                    user.setUsername(userUsername);
                    user.setPassword(userPassword);
                    user.setName(userName);
                    user.setLastname(userLastname);
                    user.setEmail(userEmail);
                    user.setPersonalCode(userPersonalCode);

                    return user;
                }
            }
        } catch (SQLException sql) {
            log.error(sql.toString());
        }


        return null;
    }

    @Override
    public boolean checkIfPasswordMatches(String password, String userDatabasePassword, SecurityUtils securityUtils) {
        return securityUtils.sha512Hash(password).equals(userDatabasePassword);
    }

    @Override
    public void registerNewUser(String username, String password, String name, String lastname, String email, String personalCode) {

        String query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)", UTC_USERS_TABLE, UTC_USERS_TABLE_USERNAME, UTC_USERS_TABLE_PASSWORD, UTC_USERS_TABLE_NAME, UTC_USERS_TABLE_LASTNAME, UTC_USERS_TABLE_EMAIL, UTC_USERS_TABLE_PERSONAL_CODE);

        try {
            PreparedStatement statement = databaseConfig.connectionToDatabase().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, lastname);
            statement.setString(5, email);
            statement.setString(6, personalCode);

            statement.execute();
            databaseConfig.closeConnection();
        } catch (SQLException sql) {
            log.error(sql.toString());
        }

    }

//    @Override
//    public void deleteUserByUsername(String username) {
//
//        try {
//            Statement statement = databaseConfig.connectionToDatabase().createStatement();
//            statement.executeUpdate(String.format("DELETE FROM %s WHERE username = '%s'", UTC_USERS_TABLE, username));
//            databaseConfig.closeConnection();
//        } catch (SQLException sql) {
//            log.error(sql.toString());
//        }
//    }
}
