package meniu;

import entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import utils.InputUtils;
import utils.SecurityUtils;

import java.io.IOException;
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

    private SecurityUtils securityUtils;

    public LoginMenuActions(UserRepository userRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, PropertyRepository propertyRepository, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.propertyRepository = propertyRepository;
        this.securityUtils = securityUtils;
    }

    public void mainMenuActions() {

        try {
            String choice = "not assigned";

            while (!choice.equals("0")) {
                OUT.write("""

                        Urban Taxes System

                        1.Sign up
                        2.Register
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
                    OUT.write("Empty IN detected, type again".getBytes());
                }
            }
        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    public void login() {

        try {
            OUT.write("\nEnter username: ".getBytes());
            String username = scanner.nextLine();

            if (!StringUtils.isBlank(username)) {
                if (userRepository.checkIfUserExists(username)) {
                    OUT.write(String.format("%nEnter password for %s: ", username).getBytes());
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

        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    public void preRegister() {

        try {
            OUT.write("\nEnter username: ".getBytes());
            String username = scanner.nextLine();

            if (userRepository.checkIfUserExists(username)) {
                loginGate(username);
            }
            register(username);
        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    private void loginGate(String username) {

        try {
            String password = scanner.nextLine();
            User user = userRepository.getUserByUsername(username);
            String userDbPassword = user.getPassword();

            if (userRepository.checkIfPasswordMatches(password, userDbPassword, securityUtils)) {
                OUT.write(String.format("%n(logged in as '%s')", username).getBytes());
                AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, user);
                accountMenuActions.accountMenuActions();
            } else {
                OUT.write("\nWrong password".getBytes());
            }

        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    public void register(String username) {

        try {
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
                OUT.write("Invalid user IN detected".getBytes());
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

            AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, user);
            accountMenuActions.accountMenuActions();

        } catch (IOException io) {
            log.error(io.toString());
        }

    }

}
