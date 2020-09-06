package main;

import config.DatabaseConfig;
import meniu.LoginMenuActions;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import security.SecurityUtils;

public class Menu {

    public static void main(String[] args) {

        DatabaseConfig databaseConfig = new DatabaseConfig();

        UserRepository userRepository = new UserRepository(databaseConfig);
        IndicatorRepository indicatorRepository = new IndicatorRepository(databaseConfig);
        UtilityRepository utilityRepository = new UtilityRepository(databaseConfig);
        PropertyRepository propertyRepository = new PropertyRepository(databaseConfig);

        SecurityUtils securityUtils = new SecurityUtils();

        LoginMenuActions loginMenuActions = new LoginMenuActions(userRepository, indicatorRepository, utilityRepository, propertyRepository, securityUtils);
        loginMenuActions.mainMenuActions();

    }
}
