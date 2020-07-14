package meniu;

import config.DatabaseConfig;
import entities.User;
import org.apache.commons.lang3.ArrayUtils;
import repositories.PropertyRepository;
import repositories.UserRepository;
import utility.InputVadility;

import java.sql.SQLException;
import java.util.Scanner;

import static config.SystemConstants.*;

public class MeniuActions {

    public static void mainMenuActions() throws SQLException {

        UserRepository userRepository = new UserRepository(new DatabaseConfig());
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
            case "1":
                login(scanner, userRepository);
                break;
            case "2":
                register(scanner, userRepository);
                break;
        }
    }

    public static void login(Scanner scanner, UserRepository userRepository) throws SQLException {

        System.out.print("\nEnter username: ");

        String username = scanner.nextLine();
        if (userRepository.checkIfUserAlreadyExists(username) != null) {
            System.out.print("\nEnter password for " + username + " : ");
            String password = scanner.nextLine();

            if (userRepository.checkIfPasswordMatches(username, password)) {
                Meniu.loggedInMenu(new User(
                        username,
                        password,
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                );
            }
        } else {
            System.out.print("\nUsername doesn't exist, would you like to register an account? (y)");
            if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registerFromSignup(scanner, userRepository, username);
            } else {
                Meniu.mainMenu();
            }
        }
    }

    public static void loginFromRegister(Scanner scanner, UserRepository userRepository, String username) throws SQLException {

        int currentTry = 1;
        int maximumRetires = 4;

        while (currentTry < maximumRetires) {
            System.out.print("\nEnter password for " + username + " : ");
            String password = scanner.nextLine();

            if (userRepository.checkIfPasswordMatches(username, password)) {
                Meniu.loggedInMenu(new User(
                        username,
                        password,
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                );
            }
            currentTry++;
            System.out.println("Failed to login , try again (" + (maximumRetires - currentTry) + ")");
        }

        System.out.println("\nFailed to login, returning to main menu ...");
        Meniu.mainMenu();
    }

    public static void register(Scanner scanner, UserRepository userRepository) throws SQLException {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username) != null) {
            
            System.out.println(username + " already exists, try to login ...");
            loginFromRegister(scanner, userRepository, username);
        } else {

            System.out.print("\nEnter password: ");
            String password = scanner.nextLine();

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

            if (userRepository.checkIfUserAlreadyExists(username) != null) {
                System.out.println(username + " already exists, try to login ...");
                loginFromRegister(scanner, userRepository, username);
            }
            userRepository.registerNewUser(username, password, name, lastname, email, personalCode);
            Meniu.loggedInMenu(new User(username, password, name, lastname, email, personalCode));
        }
    }

    public static void registerFromSignup(Scanner scanner, UserRepository userRepository, String username) throws SQLException {

        System.out.print("\nEnter password: ");
        String password = scanner.nextLine();

        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();

        System.out.print("\nEnter lastname: ");
        String lastname = scanner.nextLine();

        System.out.print("\nEnter email: ");
        String email = scanner.nextLine();

        System.out.print("\nEnter personal code: ");
        String personalCode = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username) != null) {
            throw new IllegalArgumentException(username + " already exists");
        }

        InputVadility iv = new InputVadility();
        if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(username, password, name, lastname, email, personalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        userRepository.registerNewUser(username, password, name, lastname, email, personalCode);
        Meniu.loggedInMenu(new User(username, password, name, lastname, email, personalCode));
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
