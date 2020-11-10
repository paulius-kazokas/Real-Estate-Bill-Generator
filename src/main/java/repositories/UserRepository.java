package repositories;

import config.DatabaseConfig;
import entities.User;
import interfaces.IUserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import utils.InputUtils;
import utils.SecurityUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.SystemConstants.*;

@Slf4j
public class UserRepository implements IUserRepository {

    private DatabaseConfig databaseConfig;

    public UserRepository(DatabaseConfig dc) {
        this.databaseConfig = dc;
    }

    private boolean userValidity(String param) throws SQLException {
        return !param.isBlank() && checkIfUserExists(param);
    }

    private User userObject(Integer id, String username, String password, String name, String lastname, String email, String personalCode) {
        User user = User.object();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPersonalCode(personalCode);

        return user;
    }

    @Override
    public Integer getUserId(String username) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT id FROM utc.user WHERE username = '%s'", username));

        if (resultSet != null && resultSet.next()) {
            int id = resultSet.getInt("id");
            resultSet.close();
            return id;
        }

        return null;
    }

    @Override
    public boolean checkIfUserExists(String username) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT username FROM utc.user WHERE username = '%s'", username));

        if (resultSet != null && resultSet.next()) {
            String receivedUsername = resultSet.getString("username");
            resultSet.close();
            return !receivedUsername.isBlank() && receivedUsername.equals(username);
        }

        return false;
    }

    @SneakyThrows(IOException.class)
    @Override
    public User getUserByUsername(String username) throws SQLException {

        if (userValidity(username)) {

            ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.user WHERE username = '%s'", username));

            if (resultSet.next()) {

                Integer userId = getUserId(username);
                String userPassword = resultSet.getString("password");
                String userName = resultSet.getString("name");
                String userLastname = resultSet.getString("lastname");
                String userEmail = resultSet.getString("email");
                String userPersonalCode = resultSet.getString("personal_code");
                resultSet.close();

                if (InputUtils.validArray(new String[]{userId.toString(), userPassword, username, userName, userLastname, userEmail, userPersonalCode})) {
                    return userObject(userId, username, userPassword, userName, userLastname, userEmail, userPersonalCode);
                }
            }
        }

        OUT.write("Invalid username provided".getBytes());
        return null;
    }

    @Override
    public User getUserByPropertyId(Integer id) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.user WHERE personal_code = (SELECT personal_code FROM utc.property WHERE id = %s)", id));

        if (resultSet.next()) {
            User user = User.object();
            user.setId(id);
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setName(resultSet.getString("name"));
            user.setLastname(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setPersonalCode(resultSet.getString("personal_code"));
        }

        return null;
    }

    @Override
    public void registerNewUser(String username, String password, String name, String lastname, String email, String personalCode) throws SQLException {

        String query = "INSERT INTO utc.user (username, password, name, lastname, email, personal_code) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = databaseConfig.connectionToDatabase();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastname);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, personalCode);

            preparedStatement.execute();
        }
        connection.close();
    }

}
