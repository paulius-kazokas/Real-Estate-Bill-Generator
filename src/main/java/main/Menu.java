package main;

import config.DatabaseConfig;
import entities.User;
import meniu.AccountMenuActions;
import meniu.LoginMenuActions;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import security.SecurityUtils;

import java.util.Scanner;

public class Menu {

    public Menu() {
        // empty constructor
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DatabaseConfig databaseConfig = new DatabaseConfig();
        UserRepository userRepository = new UserRepository(databaseConfig);
        IndicatorRepository indicatorRepository = new IndicatorRepository(databaseConfig);
        UtilityRepository utilityRepository = new UtilityRepository(databaseConfig);
        SecurityUtils securityUtils = new SecurityUtils();

        LoginMenuActions loginMenuActions = new LoginMenuActions(scanner, userRepository, indicatorRepository, utilityRepository, securityUtils);
        Menu menu = new Menu();

        menu.init(loginMenuActions);
    }

    public void init(LoginMenuActions loginMenuActions) {
        Menu menu = new Menu();
        menu.mainMenu(loginMenuActions);
    }

    public void mainMenu(LoginMenuActions loginMenuActions) {
        System.out.print("\nUrban Taxes System\n\n" +
                "\n1.Sign up" +
                "\n2.Register" +
                "\n0.Exit\n");
        loginMenuActions.mainMenuActions();
    }

    public void loggedInMenu(LoginMenuActions loginMenuActions, User user) {
        System.out.print("\n1.Check my properties" +
                "\n2.Check my indicators" +
                "\n3.Check my bills" +
                "\n4.Check my account info" +
                "\n0.Log out");

        DatabaseConfig databaseConfig = new DatabaseConfig();
        PropertyRepository propertyRepository = new PropertyRepository(databaseConfig);
        IndicatorRepository indicatorRepository = new IndicatorRepository(databaseConfig);
        UtilityRepository utilityRepository = new UtilityRepository(databaseConfig);

        AccountMenuActions accountMenuActions = new AccountMenuActions(loginMenuActions, propertyRepository, indicatorRepository, utilityRepository, user);
        accountMenuActions.accountMenuActions(user);
    }

}
