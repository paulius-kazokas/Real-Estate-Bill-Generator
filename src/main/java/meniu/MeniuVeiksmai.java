package meniu;

import duomenubaze.DatabaseActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class MeniuVeiksmai {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeniuVeiksmai.class);

    public static void pagrindinioMeniuPasirinkimai() throws SQLException  {

        DatabaseActions da = new DatabaseActions();
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("Choice: ");
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
        LOGGER.info("Enter username: ");
        String username = scanner.nextLine();

        if(da.checkIfUserExists(da.connectionToDatabase(), username) != null) {
            signup(scanner, da, username);
        } else {
            LOGGER.info("Username doesn't exist, would you like to register an account? (y/n)");
            if(scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registration(scanner, da);
            }
            System.exit(0);
        }
    }

    public static void signup(Scanner scanner, DatabaseActions da, String username) throws SQLException {
        LOGGER.info("Enter password for {}: ", username);
        String password = scanner.nextLine();
        int maximumUnsuccessfullRetries = 4;
        int unsuccessfulLogin = 1;
        boolean rightPassword = false;
        if(da.checkIfPasswordMatches(da.connectionToDatabase(), username, password)) {
            LOGGER.info("Welcome, {}", username);
            Meniu.pagrindinisPaskyrosMeniu();
        } else {
            LOGGER.warn("Password is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));
            while(unsuccessfulLogin < maximumUnsuccessfullRetries) {
                LOGGER.warn("Try again, enter password: ");
                String unsuccessfullPassword = scanner.nextLine();
                if(da.checkIfPasswordMatches(da.connectionToDatabase(), username, unsuccessfullPassword)) {
                    unsuccessfulLogin = maximumUnsuccessfullRetries;
                    rightPassword = true;
                } else {
                    unsuccessfulLogin++;
                }
            }
            if(rightPassword) {
                LOGGER.info("Welcome, '{}'", username);
                Meniu.pagrindinisPaskyrosMeniu();
            } else {
                LOGGER.error("({}) Maximum retries exceeded..", unsuccessfulLogin);
                Meniu.pagrindinisMeniu();
            }
        }
    }

    public static void registration(Scanner scanner, DatabaseActions da) throws SQLException {
        LOGGER.info("Enter username: ");
        String regUsername = scanner.nextLine();

        LOGGER.info("Enter password: ");
        String regPassword = scanner.nextLine();

        LOGGER.info("Enter name: ");
        String regName = scanner.nextLine();

        LOGGER.info("Enter lastname: ");
        String regLastname = scanner.nextLine();

        LOGGER.info("Enter email: ");
        String regEmail = scanner.nextLine();

        LOGGER.info("Enter personal code: ");
        String regPersonalCode = scanner.nextLine();

        da.registerNewUser(da.connectionToDatabase(), regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);

        LOGGER.info("Registration information: {}, {}, {}, {}, {}, {}", regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.pagrindinisMeniu();
    }
}
