package meniu;

import duomenubaze.MySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class MeniuVeiksmai {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeniuVeiksmai.class);

    public static void pagrindinioMeniuPasirinkimai() throws SQLException  {

        MySQL m = new MySQL();

        Scanner choiseScanner = new Scanner(System.in);
        LOGGER.info("Choice: ");
        String choice = choiseScanner.nextLine();

        switch(choice) {
            case "0":
                System.exit(0);
            case "1":  // prisijungimas
                // TODO: set other ends to redirect
                Scanner usernameScanner = new Scanner(System.in);
                LOGGER.info("Enter username: ");
                String username = usernameScanner.nextLine();

                if(m.checkIfUserExists(m.connectionToDatabase(), username) != null) {
                    Scanner passwordScanner = new Scanner(System.in);
                    LOGGER.info("Enter password: ");
                    String password = passwordScanner.nextLine();
                    int maximumUnsuccessfullRetries = 4;
                    int unsuccessfulLogin = 1;
                    boolean rightPassword = false;
                    if(m.checkIfPasswordMatches(m.connectionToDatabase(), username, password)) {
                        LOGGER.info("Welcome, {}", username);
                        Meniu.pagrindinisPaskyrosMeniu();
                    } else {
                        LOGGER.warn("Password is incorrect. ({}) retries left..", (maximumUnsuccessfullRetries - 1));
                        while(unsuccessfulLogin < maximumUnsuccessfullRetries) {
                            Scanner unsuccessfulPasswordScanner = new Scanner(System.in);
                            LOGGER.warn("Try again, enter password: ");
                            String unsuccessfullPassword = unsuccessfulPasswordScanner.nextLine();
                            if(m.checkIfPasswordMatches(m.connectionToDatabase(), username, unsuccessfullPassword)) {
                                unsuccessfulLogin = maximumUnsuccessfullRetries;
                                rightPassword = true;
                            } else {
                                unsuccessfulLogin++;
                            }
                        }
                        if(rightPassword = true) {
                            LOGGER.info("Welcome, '{}'", username);
                            Meniu.pagrindinisPaskyrosMeniu();
                        } else {
                            LOGGER.error("({}) Maximum retries exceeded..", unsuccessfulLogin);
                            Meniu.pagrindinisMeniu();
                        }
                    }
                } else {
                    LOGGER.info("Username doesn't exist, would you like to register an account? (y/n)");
                    System.exit(0);
                }
                break;
            case "2":  // registracija

                Scanner registerUsername = new Scanner(System.in);
                LOGGER.info("Enter username: ");
                String regUsername = registerUsername.nextLine();

                Scanner registerPassword = new Scanner(System.in);
                LOGGER.info("Enter password: ");
                String regPassword = registerPassword.nextLine();

                Scanner registerName = new Scanner(System.in);
                LOGGER.info("Enter name: ");
                String regName = registerName.nextLine();

                Scanner registerLastname = new Scanner(System.in);
                LOGGER.info("Enter lastname: ");
                String regLastname = registerLastname.nextLine();

                Scanner registerLEmail = new Scanner(System.in);
                LOGGER.info("Enter email: ");
                String regEmail = registerLEmail.nextLine();

                Scanner registerPersonalCode = new Scanner(System.in);
                LOGGER.info("Enter personal code: ");
                String regPersonalCode = registerPersonalCode.nextLine();

                m.registerNewUser(m.connectionToDatabase(), regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);

                LOGGER.info("Registration information: {}, {}, {}, {}, {}, {}", regUsername, regPassword, regName, regLastname, regEmail, regPersonalCode);
                break;
        }
    }
}
