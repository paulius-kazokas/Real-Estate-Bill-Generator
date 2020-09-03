package meniu;

import entities.User;
import main.Menu;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import repositories.IndicatorRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import security.SecurityUtils;
import utility.InputVadility;

import java.util.Scanner;

public class LoginMenuActions {

    private Scanner scanner;
    private UserRepository userRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private SecurityUtils securityUtils;
    private Menu menu;

    public LoginMenuActions(Scanner scanner, UserRepository userRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, SecurityUtils securityUtils, Menu menu) {
        this.scanner = scanner;
        this.userRepository = userRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.securityUtils = securityUtils;
        this.menu = menu;
    }

    public void mainMenuActions(LoginMenuActions lma) {

        System.out.print("\nUrban Taxes System\n\n" +
                "\n1.Sign up" +
                "\n2.Register" +
                "\n0.Exit" +
                "\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
                break;
            case "1":
                lma.login(lma);
                break;
            case "2":
                lma.preRegister(lma);
                break;
            default:
                System.out.println("LoginMenuActions.mainMenuActions() failed.");
                break;
        }

    }

    public void login(LoginMenuActions ma) {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (!StringUtils.isBlank(username)) {
            if (userRepository.checkIfUserExists(username)) {
                System.out.print("\nEnter password for " + username + " : ");
                loginGate(ma, menu, username);
            } else {
                System.out.println("\nUser does not exist");
                menu.mainMenu(ma);
            }
        } else {
            System.out.print("\nUsername doesn't exist, would you like to register an account? (y)");
            if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                ma.register(username, ma, menu);
            } else {
                menu.mainMenu(ma);
            }
        }
    }

    public void preRegister(LoginMenuActions ma) {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (userRepository.checkIfUserExists(username)) {
            System.out.println(username + " already exists, try to login ...\nEnter password for " + username + " : ");
            loginGate(ma, menu, username);
        } else {
            ma.register(username, ma, menu);
        }
    }

    private void loginGate(LoginMenuActions ma, Menu menu, String username) {

        String password = scanner.nextLine();
        User user = userRepository.getUserByUsername(username);
        String userDbPassword = user.getPassword();

        if (userRepository.checkIfPasswordMatches(password, userDbPassword, securityUtils)) {
            System.out.print("\n(logged in as '" + username + "')");
            menu.loggedInMenu(ma, user, menu);
        } else {
            System.out.println("\nWrong password");
            menu.mainMenu(ma);
        }
    }

    public void register(String username, LoginMenuActions ma, Menu menu) {

        System.out.print("\nEnter password: ");
        String password = scanner.nextLine();
        String hashedPassword = securityUtils.sha512Hash(password);

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

        userRepository.registerNewUser(username, hashedPassword, name, lastname, email, personalCode);
        System.out.print("\n(logged in as '" + username + "')");
        menu.loggedInMenu(ma, new User(username, hashedPassword, name, lastname, email, personalCode), menu);
    }

}