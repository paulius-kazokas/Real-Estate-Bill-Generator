package meniu;

import config.DatabaseConfig;
import entities.User;
import org.apache.commons.lang3.ArrayUtils;
import repositories.PropertyRepository;
import repositories.UserRepository;
import security.SecurityUtils;
import utility.InputVadility;

import java.sql.SQLException;
import java.util.Scanner;

import static config.SystemConstants.*;

public class MeniuActions {

    public static void mainMenuActions() throws SQLException {

        SecurityUtils securityUtils = new SecurityUtils();
        UserRepository userRepository = new UserRepository(new DatabaseConfig());
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
            case "1":
                login(scanner, userRepository, securityUtils);
                break;
            case "2":
                register(scanner, userRepository, securityUtils);
                break;
        }
    }

    public static void login(Scanner scanner, UserRepository userRepository, SecurityUtils securityUtils) throws SQLException {

        System.out.print("\nEnter username: ");

        String username = scanner.nextLine();
        if (userRepository.checkIfUserAlreadyExists(username)) {
            int currentTry = 1;
            int maximumRetires = 5;

            while (currentTry < maximumRetires) {
                System.out.print("\nEnter password for " + username + " : ");
                String password = scanner.nextLine();

                String userDbPassword = userRepository.getInformationByUsername(username, "password");
                String decryptedUserDbPassword = securityUtils.decrypt(userDbPassword);

                if (decryptedUserDbPassword.equals(password)) {
                    Meniu.loggedInMenu(new User(
                            username,
                            password,
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                    );
                } else {
                    currentTry++;
                    System.out.println("Failed to login , try again (" + (maximumRetires - currentTry) + ")");
                }
            }

            System.out.println("\nFailed to login, returning to main menu ...");
            Meniu.mainMenu();
        } else {
            System.out.print("\nUsername doesn't exist, would you like to register an account? (y)");
            if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registerFromSignup(scanner, userRepository, username, securityUtils);
            } else {
                Meniu.mainMenu();
            }
        }
    }

    public static void loginFromRegister(Scanner scanner, UserRepository userRepository, String username, SecurityUtils securityUtils) throws SQLException {

        int currentTry = 1;
        int maximumRetires = 5;

        while (currentTry < maximumRetires) {
            System.out.print("\nEnter password for " + username + " : ");
            String password = scanner.nextLine();

            String userDbPassword = userRepository.getInformationByUsername(username, "password");
            String decryptedUserDbPassword = securityUtils.decrypt(userDbPassword);

            if (decryptedUserDbPassword.equals(password)) {
                Meniu.loggedInMenu(new User(
                        username,
                        password,
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                );
            } else {
                currentTry++;
                System.out.println("Failed to login , try again (" + (maximumRetires - currentTry) + ")");
            }
        }

        System.out.println("\nFailed to login, returning to main menu ...");
        Meniu.mainMenu();
    }

    public static void register(Scanner scanner, UserRepository userRepository, SecurityUtils securityUtils) throws SQLException {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username)) {
            System.out.println(username + " already exists, try to login ...");
            loginFromRegister(scanner, userRepository, username, securityUtils);
        } else {

            System.out.print("\nEnter password: ");
            String password = scanner.nextLine();
            String encryptedPassword = securityUtils.encrypt(password);

            System.out.print("\nEnter name: ");
            String name = scanner.nextLine();

            System.out.print("\nEnter lastname: ");
            String lastname = scanner.nextLine();

            System.out.print("\nEnter email: ");
            String email = scanner.nextLine();

            System.out.print("\nEnter personal code: ");
            String personalCode = scanner.nextLine();

            InputVadility iv = new InputVadility();
            if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(username, password, name, lastname, email, personalCode))) {
                throw new IllegalArgumentException("Invalid user input detected");
            }

            userRepository.registerNewUser(username, encryptedPassword, name, lastname, email, personalCode);
            Meniu.loggedInMenu(new User(username, encryptedPassword, name, lastname, email, personalCode));
        }
    }

    public static void registerFromSignup(Scanner scanner, UserRepository userRepository, String username, SecurityUtils securityUtils) throws SQLException {

        System.out.print("\nEnter password: ");
        String password = scanner.nextLine();
        String encryptedPassword = securityUtils.encrypt(password);

        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();

        System.out.print("\nEnter lastname: ");
        String lastname = scanner.nextLine();

        System.out.print("\nEnter email: ");
        String email = scanner.nextLine();

        System.out.print("\nEnter personal code: ");
        String personalCode = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username)) {
            throw new IllegalArgumentException(username + " already exists");
        }

        InputVadility iv = new InputVadility();
        if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(username, password, name, lastname, email, personalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        userRepository.registerNewUser(username, encryptedPassword, name, lastname, email, personalCode);
        Meniu.loggedInMenu(new User(username, encryptedPassword, name, lastname, email, personalCode));
    }

    public static void loggedInMenuActions(User user) throws SQLException {

        PropertyRepository pr = new PropertyRepository(new DatabaseConfig());

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                Meniu.mainMenu();
            case "1":
                System.out.println(pr.getUserProperties(user.getPersonalCode()));
                break;
        }
        MeniuActions.loggedInMenuActions(user);
    }

}
