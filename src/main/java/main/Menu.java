package main;

import config.DatabaseConfig;
import meniu.LoginMenuActions;
import repositories.*;
import utils.SecurityUtils;

public class Menu {

    public static void main(String[] args) {

        DatabaseConfig databaseConfig = new DatabaseConfig();

        UserRepository userRepository = new UserRepository(databaseConfig);
        UtilityProviderRepository utilityProviderRepository = new UtilityProviderRepository(databaseConfig);
        UtilityRepository utilityRepository = new UtilityRepository(utilityProviderRepository, databaseConfig);
        IndicatorRepository indicatorRepository = new IndicatorRepository(utilityRepository, databaseConfig);
        PropertyRepository propertyRepository = new PropertyRepository(userRepository, indicatorRepository, databaseConfig);
        //BillRepository billRepository = new BillRepository(propertyRepository);

        SecurityUtils securityUtils = new SecurityUtils();

        LoginMenuActions loginMenuActions = new LoginMenuActions(userRepository, indicatorRepository, utilityRepository, propertyRepository, securityUtils);
        loginMenuActions.mainMenuActions();

    }
}
