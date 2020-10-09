package interfaces;

import entities.User;
import utils.SecurityUtils;

public interface IUserRepository {

    boolean checkIfUserExists(String usernameInput);

    Integer getUserId(String username);

    User getUserByUsername(String username);

    User getUser(String personalCode);

    boolean checkIfPasswordMatches(String password, String storedPassword, SecurityUtils securityUtils);

    void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode);

}
