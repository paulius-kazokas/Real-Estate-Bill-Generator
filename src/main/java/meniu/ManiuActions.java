package meniu;

import database.DatabaseActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class ManiuActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManiuActions.class);

    public static void mainMenuActions() throws SQLException  {

        DatabaseActions da = new DatabaseActions();
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("\nChoice: ");
        String choice = scanner.nextLine();

        switch(choice) {
            case "0":
                System.exit(0);
            case "1":
                signupBase(scanner);
                break;
            case "2":
                registration(scanner, da);
                break;
        }
    }

    public static void signupBase(Scanner scanner) throws SQLException {

        DatabaseActions da = new DatabaseActions();
        LOGGER.info("\nEnter username: ");
        String username = scanner.nextLine();

        if(da.checkIfUserExists(da.connectionToDatabase(), username) != null) {
            signup(scanner, da, username);
        } else {
            LOGGER.info("\nUsername doesn't exist, would you like to register an account? (y/n)");
            if(scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registration(scanner, da);
            } else {
                System.exit(0);
            }
        }
    }

    public static void signup(Scanner scanner, DatabaseActions da, String username) throws SQLException {

        LOGGER.info("\nEnter password for {}: ", username);
        String password = scanner.nextLine();
        int maximumUnsuccessfullRetries = 4;
        int unsuccessfulLogin = 1;
        boolean rightPassword = false;

        if(da.checkIfPasswordMatches(da.connectionToDatabase(), username, password)) {
            LOGGER.info("\nWelcome, {}", username);
            Meniu.mainAccountMenu();
        } else {
            LOGGER.warn("\nPassword is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));

            while(unsuccessfulLogin < maximumUnsuccessfullRetries) {
                LOGGER.warn("\nTry again, enter password ({}): ", unsuccessfulLogin);
                String unsuccessfullPassword = scanner.nextLine();
                if(da.checkIfPasswordMatches(da.connectionToDatabase(), username, unsuccessfullPassword)) {
                    unsuccessfulLogin = maximumUnsuccessfullRetries;
                    rightPassword = true;
                } else {
                    unsuccessfulLogin++;
                }
            }

            if(rightPassword) {
                LOGGER.info("\nWelcome, '{}'", username);
                Meniu.mainAccountMenu();
            } else {
                LOGGER.error("\n({}) Maximum retries exceeded, returning to the main menu..", unsuccessfulLogin);
                Meniu.mainMenu();
            }
        }
    }

    public static void registration(Scanner scanner, DatabaseActions da) throws SQLException {

        LOGGER.info("\nEnter username: ");
        String regUsername = scanner.nextLine();

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

        da.registerNewUser(da.connectionToDatabase(), regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);

        LOGGER.info("\nRegistration information: {}, {}, {}, {}, {}, {}", regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.mainMenu();
    }
}
