package interfaces;

import entities.User;

import java.sql.SQLException;

public interface IUserRepository {

    boolean checkIfUserExists(String usernameInput) throws SQLException;

    Integer getUserId(String username) throws SQLException;

    User getUserByUsername(String username) throws SQLException;

    User getUserByPropertyId(Integer id) throws SQLException;

    void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode) throws SQLException;

    User getUserByPersonalCode(String personalCode) throws SQLException;

}
