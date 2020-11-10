package main;

import config.DatabaseConfig;
import meniu.LoginMenuActions;
import repositories.*;
import utils.SecurityUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Menu {

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {

        DatabaseConfig databaseConfig = new DatabaseConfig();

        UserRepository userRepository = new UserRepository(databaseConfig);
        UtilityProviderRepository utilityProviderRepository = new UtilityProviderRepository(databaseConfig);
        UtilityRepository utilityRepository = new UtilityRepository(utilityProviderRepository, databaseConfig);
        PropertyRepository propertyRepository = new PropertyRepository(userRepository, databaseConfig);
        IndicatorRepository indicatorRepository = new IndicatorRepository(propertyRepository, utilityRepository, databaseConfig);
        BillRepository billRepository = new BillRepository(userRepository, databaseConfig);

        SecurityUtils securityUtils = new SecurityUtils();

        LoginMenuActions loginMenuActions = new LoginMenuActions(userRepository, indicatorRepository, utilityRepository, propertyRepository, billRepository, securityUtils);
        loginMenuActions.mainMenuActions();

    }
}
