package meniu;

import duomenubaze.MySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class MeniuVeiksmai {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeniuVeiksmai.class);

    public static void pagrindinioMeniuPasirinkimai() throws SQLException  {

        Scanner scanner = new Scanner(System.in);
        LOGGER.info("Choice: ");
        String choice = scanner.nextLine();

        switch(choice) {
            case "0":
                System.exit(0);
            case "1":
                signup(scanner);
                break;
            case "2":
                registration(scanner);
                break;
        }
    }

    public static void signup(Scanner scanner) throws SQLException {

        MySQL m = new MySQL();
        LOGGER.info("Enter username: ");
        String username = scanner.nextLine();

        if(m.checkIfUserExists(m.connectionToDatabase(), username) != null) {
            LOGGER.info("Enter password for {}: ", username);
            String password = scanner.nextLine();
            int maximumUnsuccessfullRetries = 4;
            int unsuccessfulLogin = 1;
            boolean rightPassword = false;
            if(m.checkIfPasswordMatches(m.connectionToDatabase(), username, password)) {
                LOGGER.info("Welcome, {}", username);
                Meniu.pagrindinisPaskyrosMeniu();
            } else {
                LOGGER.warn("Password is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));
                while(unsuccessfulLogin < maximumUnsuccessfullRetries) {
                    LOGGER.warn("Try again, enter password: ");
                    String unsuccessfullPassword = scanner.nextLine();
                    if(m.checkIfPasswordMatches(m.connectionToDatabase(), username, unsuccessfullPassword)) {
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
        } else {
            LOGGER.info("Username doesn't exist, would you like to register an account? (y/n)");
            if(scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {

            }
            System.exit(0);
        }
    }

    public static void registration(Scanner scanner) throws SQLException {
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

        MySQL m = new MySQL();
        m.registerNewUser(m.connectionToDatabase(), regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);

        LOGGER.info("Registration information: {}, {}, {}, {}, {}, {}", regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
        Meniu.pagrindinisMeniu();
    }
}
