package meniu;

import config.DatabaseConfig;
import data.DataGenerator;
import database.DatabaseActions;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.InputVadility;

import java.sql.SQLException;
import java.util.Scanner;

public class MeniuActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeniuActions.class);

    public static void mainMenuActions() throws SQLException {

        DatabaseActions da = new DatabaseActions(new DatabaseConfig());
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
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

        if (da.getUserByUsername(username) != null) {
            signup(scanner, da, username);
        } else {
            LOGGER.info("\nUsername doesn't exist, would you like to register an account? (y)");
            if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
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

        if (da.checkIfPasswordMatches(username, password)) {
            System.out.print("\nWelcome, " + username);
            Meniu.mainAccountMenu(username);
        } else {
            LOGGER.warn("Password is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));

            while (unsuccessfulLogin < maximumUnsuccessfullRetries) {
                System.out.print("Try again, enter password (" + unsuccessfulLogin + "): ");
                String unsuccessfullPassword = scanner.nextLine();
                if (da.checkIfPasswordMatches(username, unsuccessfullPassword)) {
                    unsuccessfulLogin = maximumUnsuccessfullRetries;
                    rightPassword = true;
                } else {
                    unsuccessfulLogin++;
                }
            }

            if (rightPassword) {
                System.out.print("\nWelcome, " + username);
                Meniu.mainAccountMenu(username);
            } else {
                System.out.print("\nFailed to login, returning to main menu..");
                Meniu.mainMenu();
            }
        }
    }

    public static void registration(Scanner scanner, DatabaseActions da) throws SQLException {

        InputVadility iv = new InputVadility();

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

        if (!da.getUserByUsername(regUsername).isBlank()) {
            throw new IllegalArgumentException(regUsername + " already exists");
        }

        if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        da.registerNewUser(regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.mainMenu();
    }

    public static void registrationNonExistingUser(Scanner scanner, DatabaseActions da, String nonExistingUsername) throws SQLException {

        InputVadility iv = new InputVadility();

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

        if (!da.getUserByUsername(nonExistingUsername).isBlank()) {
            throw new IllegalArgumentException(nonExistingUsername + " already exists");
        }

        if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(nonExistingUsername, regPassword, regName, regLastname, regEmail, regPersonalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        da.registerNewUser(nonExistingUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.mainMenu();
    }

    public static void mainAccountMenuActions(String username) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
            case "1":
                accountMonthlyBillActions(scanner, username);
                break;
            case "2":
                accountSpecificUtilityReportActions(scanner, username);
                break;
            case "3":
                accountViewActions(scanner, username);
                break;
        }
    }

    public static void accountMonthlyBillActions(Scanner scanner, String username) throws SQLException {

        /* shows report
         * press 1 pay bill
         * press 2 correct bill - send a request to admin for bill correction (upon utility indicators not matching with users utility device indicators)
         * press 3 pay part of the bill
         * press 4 cancel bill - send a request to admin that bill is already payed/got bill on mistake
         * press 5 to go back to mainAccountMenuActions
         */

        // print total amount for the month
        DataGenerator dg = new DataGenerator();
        dg.generateUserMonthlyBillReport("456");

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
            case "1":
                accountMonthlyBillActions(scanner, username);
                break;
            case "2":
                accountSpecificUtilityReportActions(scanner, username);
                break;
            case "3":
                accountViewActions(scanner, username);
                break;
        }
    }

    public static void accountSpecificUtilityReportActions(Scanner scanner, String username) {

        /* press 1 to get a specific utility total price for current month (utility + current month)
         * press 2 to get a specific utility total for multiple months (utility + month range)
         * press 3 to get a multiple utilities total for current month (multiple utilities + current month)
         * press 4 to get a multiple utilities total for multiple months (multiple utilities + multiple months)
         * press 5 to go back to mainAccountMenuActions
         */
    }

    public static void accountViewActions(Scanner scanner, String username) {

        /* shows account minimal info
         * press 1 to edit account info
         * press 2 to get back to mainAccountMenuActions
         */
    }
}
