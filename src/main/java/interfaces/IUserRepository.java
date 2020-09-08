package interfaces;

import entities.lUser;
import entities.oUser;
import security.SecurityUtils;

public interface IUserRepository {

    boolean checkIfUserExists(String usernameInput);

    //oUser getUserByUsername(String username);
    lUser getUserByUsername(String username);

    lUser getUser(int personalCode);

    boolean checkIfPasswordMatches(String password, String storedPassword, SecurityUtils securityUtils);

    void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode);

    void deleteUserByUsername(String deleteUsername);

    String getPersonalCodeByUsername(String username);

}
