package meniu;

import entities.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import repositories.*;
import utils.InputUtils;
import utils.SecurityUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

import static config.SystemConstants.IN;
import static config.SystemConstants.OUT;

@Slf4j
public class LoginMenuActions {

    private Scanner scanner = new Scanner(IN);

    private UserRepository userRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private PropertyRepository propertyRepository;
    private BillRepository billRepository;

    private SecurityUtils securityUtils;

    public LoginMenuActions(UserRepository userRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, PropertyRepository propertyRepository, BillRepository billRepository, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.propertyRepository = propertyRepository;
        this.billRepository = billRepository;
        this.securityUtils = securityUtils;
    }

    @SneakyThrows(IOException.class)
    public void mainMenuActions() throws SQLException, NoSuchAlgorithmException {

        String choice = "not assigned";

        while (!choice.equals("0")) {
            OUT.write("""

                    Urban Taxes System

                    1.Sign in
                    2.Sign up
                    0.Exit

                    Choice: """.getBytes());
            choice = scanner.nextLine();

            if (!choice.isBlank()) {
                switch (choice) {
                    case "0" -> {
                        return;
                    }
                    case "1" -> login();
                    case "2" -> preRegister();
                    default -> OUT.write("Unexpected action".getBytes());
                }
            } else {
                OUT.write("Empty input detected, type again".getBytes());
            }
        }

    }

    @SneakyThrows(IOException.class)
    public void login() throws SQLException, NoSuchAlgorithmException {

        OUT.write("\nUsername: ".getBytes());
        String username = scanner.nextLine();

        if (!StringUtils.isBlank(username)) {
            if (userRepository.checkIfUserExists(username)) {
                OUT.write(String.format("Password for %s: ", username).getBytes());
                loginGate(username);
            } else {
                OUT.write("\nUsername doesn't exist, would you like to register an account? (y)".getBytes());
                if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                    register(username);
                } else {
                    mainMenuActions();
                }
            }
        } else {
            log.error("Invalid username");
        }

    }

    @SneakyThrows(IOException.class)
    public void preRegister() throws SQLException, NoSuchAlgorithmException {

        OUT.write("\nUsername: ".getBytes());
        String username = scanner.nextLine();

        if (userRepository.checkIfUserExists(username)) {
            loginGate(username);
        }
        register(username);

    }

    @SneakyThrows(IOException.class)
    private void loginGate(String username) throws SQLException, NoSuchAlgorithmException {

        String password = scanner.nextLine();
        User user = userRepository.getUserByUsername(username);
        String userDbPassword = user.getPassword();

        if (securityUtils.checkIfPasswordMatches(password, userDbPassword)) {
            AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, billRepository, user);
            OUT.write(String.format("Welcome, %s", username).getBytes());
            accountMenuActions.accountMenuActions();
        } else {
            OUT.write("\nWrong password".getBytes());
        }

    }

    @SneakyThrows(IOException.class)
    public void register(String username) throws SQLException, NoSuchAlgorithmException {

        OUT.write("\nEnter password: ".getBytes());
        String password = scanner.nextLine();
        String hashedPassword = securityUtils.sha512Hash(password);

        OUT.write("\nEnter name: ".getBytes());
        String name = scanner.nextLine();

        OUT.write("\nEnter lastname: ".getBytes());
        String lastname = scanner.nextLine();

        OUT.write("\nEnter email: ".getBytes());
        String email = scanner.nextLine();

        OUT.write("\nEnter personal code: ".getBytes());
        String personalCode = scanner.nextLine();

        if (!InputUtils.validArray(new String[]{username, password, name, lastname, email, personalCode})) {
            OUT.write("Invalid user input detected".getBytes());
            return;
        }

        userRepository.registerNewUser(username, hashedPassword, name, lastname, email, personalCode);
        OUT.write(String.format("%n(logged in as '%s')", username).getBytes());

        User user = User.object();
        user.setId(userRepository.getUserId(username));
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPersonalCode(personalCode);

        AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, billRepository, user);
        accountMenuActions.accountMenuActions();

    }

}
