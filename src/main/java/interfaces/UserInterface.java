package user;

import entities.User;

public interface UserInterface {

    String getUserByUsername(String usernameInput);
    boolean checkIfPasswordMatches(String usernameWithThisPassword, String userPasswordInput);
    void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode);
    void deleteUserByUsername(String deleteUsername);
    String getPersonalCodeByUsername(String username);
}
