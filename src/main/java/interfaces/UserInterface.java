package interfaces;

public interface UserInterface {

    String checkIfUserAlreadyExists(String usernameInput);

    String getInformationByUsername(String username, String criteria);

    boolean checkIfPasswordMatches(String usernameWithThisPassword, String userPasswordInput);

    void registerNewUser(String regUsername, String regPassword, String regName, String regLastName, String regEmail, String regPersonalCode);

    void deleteUserByUsername(String deleteUsername);

    String getPersonalCodeByUsername(String username);

}
