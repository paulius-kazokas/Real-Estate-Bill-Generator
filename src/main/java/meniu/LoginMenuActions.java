package meniu;

import entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import security.SecurityUtils;
import utility.InputValidity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

@Slf4j
public class LoginMenuActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMenuActions.class);

    private InputStream input = System.in;
    private OutputStream output = System.out;
    private Scanner scanner = new Scanner(input);

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

                output.write("\nUrban Taxes System\n\n\n1.Sign up\n2.Register\n0.Exit\nChoice: ".getBytes());
                choice = scanner.nextLine();

                if (!choice.isBlank()) {
                    switch (choice) {
                        case "0":
                            break;
                        case "1":
                            login();
                            break;
                        case "2":
                            preRegister();
                            break;
                        default:
                            output.write("Unexpected action".getBytes());
                            break;
                    }
                } else {
                    output.write("Empty input detected, type again".getBytes());
                }
            }
        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    public void login() {

        try {
            output.write("\nEnter username: ".getBytes());
            String username = scanner.nextLine();

            if (!StringUtils.isBlank(username)) {
                if (userRepository.checkIfUserExists(username)) {
                    output.write(String.format("%nEnter password for %s: ", username).getBytes());
                    loginGate(username);
                } else {
                    output.write("\nUsername doesn't exist, would you like to register an account? (y)".getBytes());
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
            output.write("\nEnter username: ".getBytes());
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

                output.write(String.format("%n(logged in as '%s')", username).getBytes());
                AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, user);
                accountMenuActions.accountMenuActions();
            } else {
                output.write("\nWrong password".getBytes());
            }

        } catch (IOException io) {
            log.error(io.toString());
        }

    }

    public void register(String username) {

        try {

            output.write("\nEnter password: ".getBytes());
            String password = scanner.nextLine();
            String hashedPassword = securityUtils.sha512Hash(password);

            output.write("\nEnter name: ".getBytes());
            String name = scanner.nextLine();

            output.write("\nEnter lastname: ".getBytes());
            String lastname = scanner.nextLine();

            output.write("\nEnter email: ".getBytes());
            String email = scanner.nextLine();

            output.write("\nEnter personal code: ".getBytes());
            String personalCode = scanner.nextLine();

            if (InputValidity.checkArrayForFalseItemValue(ArrayUtils.toArray(username, password, name, lastname, email, personalCode))) {
                throw new IllegalArgumentException("Invalid user input detected");
            }

            userRepository.registerNewUser(username, hashedPassword, name, lastname, email, personalCode);
            output.write(String.format("%n(logged in as '%s')", username).getBytes());

            User user = User.builder()
                    .id(userRepository.getUserId(username))
                    .username(username)
                    .password(hashedPassword)
                    .name(name)
                    .lastname(lastname)
                    .email(email)
                    .personalCode(personalCode)
                    .build();

            AccountMenuActions accountMenuActions = new AccountMenuActions(propertyRepository, indicatorRepository, utilityRepository, user);
            accountMenuActions.accountMenuActions();

        } catch (IOException io) {
            log.error(io.toString());
        }

    }

}
