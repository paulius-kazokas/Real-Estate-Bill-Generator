package meniu;

import config.DatabaseConfig;
import database.DatabaseActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class MeniuActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeniuActions.class);

    public static void mainMenuActions() throws SQLException {

        DatabaseActions da = new DatabaseActions(new DatabaseConfig());
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch(choice) {
            case "0":
                System.exit(0);
            case "1":
                signupBase(scanner, da);
                break;
            case "2":
                registration(scanner, da);
                break;
        }
    }

    public static void signupBase(Scanner scanner, DatabaseActions da) throws SQLException {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if(da.checkIfUserExists(username) != null) {
            signup(scanner, da, username);
        } else {
            LOGGER.info("\nUsername doesn't exist, would you like to register an account? (y)");
            if(scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registrationNonExistingUser(scanner, da, username);
            } else {
                Meniu.mainMenu();
            }
        }
    }

    public static void signup(Scanner scanner, DatabaseActions da, String username) throws SQLException {

        System.out.print("\nEnter password for " + username + " : ");
        String password = scanner.nextLine();
        int maximumUnsuccessfullRetries = 4;
        int unsuccessfulLogin = 1;
        boolean rightPassword = false;

        if(da.checkIfPasswordMatches(username, password)) {
            System.out.print("\nWelcome, " + username);
            Meniu.mainAccountMenu(username);
        } else {
            LOGGER.warn("Password is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));

            while(unsuccessfulLogin < maximumUnsuccessfullRetries) {
                System.out.print("Try again, enter password (" + unsuccessfulLogin + "): ");
                String unsuccessfullPassword = scanner.nextLine();
                if(da.checkIfPasswordMatches(username, unsuccessfullPassword)) {
                    unsuccessfulLogin = maximumUnsuccessfullRetries;
                    rightPassword = true;
                } else {
                    unsuccessfulLogin++;
                }
            }

            if(rightPassword) {
                System.out.print("\nWelcome, " + username);
                Meniu.mainAccountMenu(username);
            } else {
                System.out.print("\nFailed to login, returning to main menu..");
                Meniu.mainMenu();
            }
        }
    }

    public static void registration(Scanner scanner, DatabaseActions da) throws SQLException {

        System.out.print("\nEnter username: ");
        String regUsername = scanner.nextLine();

        System.out.print("\nEnter password: ");
        String regPassword = scanner.nextLine();

        System.out.print("\nEnter name: ");
        String regName = scanner.nextLine();

        System.out.print("\nEnter lastname: ");
        String regLastname = scanner.nextLine();

        System.out.print("\nEnter email: ");
        String regEmail = scanner.nextLine();

        System.out.print("\nEnter personal code: ");
        String regPersonalCode = scanner.nextLine();

        da.registerNewUser(regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.mainMenu();
    }

    public static void registrationNonExistingUser(Scanner scanner, DatabaseActions da, String nonExistingUsername) throws SQLException {

        LOGGER.info("\nEnter password: ");
        String regPassword = scanner.nextLine();

        LOGGER.info("\nEnter name: ");
        String regName = scanner.nextLine();

        LOGGER.info("\nEnter lastname: ");
        String regLastname = scanner.nextLine();

        LOGGER.info("\nEnter email: ");
        String regEmail = scanner.nextLine();

        LOGGER.info("\nEnter personal code: ");
        String regPersonalCode = scanner.nextLine();

        da.registerNewUser(nonExistingUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.mainMenu();
    }

}
