package meniu;

import config.DatabaseConfig;
import entities.Property;
import entities.User;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.ArrayUtils;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
import repositories.UtilityRepository;
import security.SecurityUtils;
import utility.InputVadility;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static config.SystemConstants.*;

public class MeniuActions {

    public static void mainMenuActions() {

        SecurityUtils securityUtils = new SecurityUtils();
        UserRepository userRepository = new UserRepository(new DatabaseConfig());
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nChoice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "0":
                System.exit(0);
            case "1":
                login(scanner, userRepository, securityUtils);
                break;
            case "2":
                register(scanner, userRepository, securityUtils);
                break;
        }
    }

    public static void login(Scanner scanner, UserRepository userRepository, SecurityUtils securityUtils) {

        System.out.print("\nEnter username: ");

        String username = scanner.nextLine();
        if (userRepository.checkIfUserAlreadyExists(username)) {
            int currentTry = 1;
            int maximumRetires = 5;

            while (currentTry < maximumRetires) {
                System.out.print("\nEnter password for " + username + " : ");
                String password = scanner.nextLine();

                String userDbPassword = userRepository.getInformationByUsername(username, "password");
                String decryptedUserDbPassword = securityUtils.decrypt(userDbPassword);

                if (decryptedUserDbPassword.equals(password)) {
                    System.out.print("\n(logged in as '" + username + "')");
                    Meniu.loggedInMenu(new User(
                            username,
                            password,
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                            userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                    );
                } else {
                    currentTry++;
                    System.out.println("Failed to login , try again (" + (maximumRetires - currentTry) + ")");
                }
            }

            System.out.println("\nFailed to login, returning to main menu ...");
            Meniu.mainMenu();
        } else {
            System.out.print("\nUsername doesn't exist, would you like to register an account? (y)");
            if (scanner.nextLine().equals("y") || scanner.nextLine().equals("Y")) {
                registerFromSignup(scanner, userRepository, username, securityUtils);
            } else {
                Meniu.mainMenu();
            }
        }
    }

    public static void loginFromRegister(Scanner scanner, UserRepository userRepository, String username, SecurityUtils securityUtils) {

        int currentTry = 1;
        int maximumRetires = 5;

        while (currentTry < maximumRetires) {
            System.out.print("\nEnter password for " + username + " : ");
            String password = scanner.nextLine();

            String userDbPassword = userRepository.getInformationByUsername(username, "password");
            String decryptedUserDbPassword = securityUtils.decrypt(userDbPassword);

            if (decryptedUserDbPassword.equals(password)) {
                System.out.print("\n(logged in as '" + username + "')");
                Meniu.loggedInMenu(new User(
                        username,
                        password,
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_NAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_LASTNAME),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_EMAIL),
                        userRepository.getInformationByUsername(username, UTC_USERS_TABLE_PERSONAL_CODE))
                );
            } else {
                currentTry++;
                System.out.println("Failed to login , try again (" + (maximumRetires - currentTry) + ")");
            }
        }

        System.out.println("\nFailed to login, returning to main menu ...");
        Meniu.mainMenu();
    }

    public static void register(Scanner scanner, UserRepository userRepository, SecurityUtils securityUtils) {

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username)) {
            System.out.println(username + " already exists, try to login ...");
            loginFromRegister(scanner, userRepository, username, securityUtils);
        } else {

            System.out.print("\nEnter password: ");
            String password = scanner.nextLine();
            String encryptedPassword = securityUtils.encrypt(password);

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

            userRepository.registerNewUser(username, encryptedPassword, name, lastname, email, personalCode);
            System.out.print("\n(logged in as '" + username + "')");
            Meniu.loggedInMenu(new User(username, encryptedPassword, name, lastname, email, personalCode));
        }
    }

    public static void registerFromSignup(Scanner scanner, UserRepository userRepository, String username, SecurityUtils securityUtils) {

        System.out.print("\nEnter password: ");
        String password = scanner.nextLine();
        String encryptedPassword = securityUtils.encrypt(password);

        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();

        System.out.print("\nEnter lastname: ");
        String lastname = scanner.nextLine();

        System.out.print("\nEnter email: ");
        String email = scanner.nextLine();

        System.out.print("\nEnter personal code: ");
        String personalCode = scanner.nextLine();

        if (userRepository.checkIfUserAlreadyExists(username)) {
            throw new IllegalArgumentException(username + " already exists");
        }

        InputVadility iv = new InputVadility();
        if (iv.checkArrayForFalseItemValue(ArrayUtils.toArray(username, password, name, lastname, email, personalCode))) {
            throw new IllegalArgumentException("Invalid user input detected");
        }

        userRepository.registerNewUser(username, encryptedPassword, name, lastname, email, personalCode);
        System.out.print("\n(logged in as '" + username + "')");
        Meniu.loggedInMenu(new User(username, encryptedPassword, name, lastname, email, personalCode));
    }

    public static void loggedInMenuActions(User user) {

        Scanner scanner = new Scanner(System.in);

        DatabaseConfig dc = new DatabaseConfig();

        Property property = new Property(user);

        PropertyRepository pr = new PropertyRepository(dc);
        IndicatorRepository ir = new IndicatorRepository(dc);
        UtilityRepository ur = new UtilityRepository(dc);

        MultiValuedMap<String, String> userProperties = userProperties(pr, property.getOwnderPersonalCode());

        if (userProperties != null) {
            System.out.print("\nChoice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    System.out.println("\nlogged out..");
                    Meniu.mainMenu();
                case "1":
                    loggedInUserProperties(userProperties);
                    break;
                case "2":
                    loggedInUserIndicators(scanner, ur, pr, ir, userProperties);
                    break;
                case "3":
                    loggedInUserBillActions();
                    break;
                case "4":
                    loggedInUserAccountInformation(user);
                    break;
            }
        } else {
            System.out.println(user.getUsername() + " doesn't have any properties, no indicator actions available");
        }

        Meniu.loggedInMenu(user);
    }

    public static MultiValuedMap<String, String> userProperties(PropertyRepository pr, String ownerPersonalCode) {
        MultiValuedMap<String, String> properties = pr.getUserProperties(ownerPersonalCode);
        return pr.userHasProperties(properties) ? properties : null;
    }

    public static void loggedInUserProperties(MultiValuedMap<String, String> userProperties) {
        System.out.println("\nProperties:\n");
        for(Map.Entry<String, String> entry : userProperties.entries()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public static void loggedInUserIndicators(Scanner scanner, UtilityRepository ur, PropertyRepository pr, IndicatorRepository ir, MultiValuedMap<String, String> userProperties) {

        // veikia bet reikia perdaryti.. I think.. stuck

        // atvaizduojami properties tipai
        System.out.println("\nProperties:");
        Map<Integer, String> userPropertyTypes = pr.getUserPropertiesCount(userProperties);
        for(Map.Entry<Integer, String> entry : userPropertyTypes.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        System.out.print("\nSelect available property type: ");
        String propertyTypeChoice = scanner.nextLine();

        // pasirenkamas properties tipas
        String chosenPropertyType = userPropertyTypes.get(Integer.valueOf(propertyTypeChoice));

        // atvaizduojami adresai pagal pasirinkta tipa
        System.out.println("\nAvailable addresses:\n");

        Map<Integer, String> chosenPropertyTypeAddresses = new LinkedHashMap<>();
        int addressCount = 1;
        for (String address : pr.getProprtyAddressByPropertyType(chosenPropertyType)) {
            System.out.println(addressCount + ". " + address);
            chosenPropertyTypeAddresses.put(addressCount, address);
            addressCount++;
        }

        // pasirenkamas adresas
        System.out.print("\nSelect address: ");
        String propertyAddressChoice = scanner.nextLine();
        String address = chosenPropertyTypeAddresses.get(Integer.valueOf(propertyAddressChoice));

        int propertyId = pr.getPropertyIdByPropertyAddress(address);
        System.out.println("propertyId: " + propertyId);

        // gauti utility name pagal indicatorid

        // atvaizduojami visi indicatoriai
        System.out.println("\nIndicators for " + address + ":");
        List<Integer> indicatorIds = ir.getIndicatorIdsByPropertyId(propertyId);
        for (int indicatorId : indicatorIds) {
            int utilityId = ir.getUtilityIdByIndicatorId(indicatorId);
            String utilityName = ur.getUtilityNameByUtilityId(utilityId);
            ir.getIndicatorMonthStartEndAmountsByIndicatorId(indicatorId).forEach(amount -> System.out.println("* " + utilityName + ": " +amount));
        }
    }

    public static void loggedInUserBillActions() {
        System.out.println("to be implemented");
    }

    public static void loggedInUserAccountInformation(User user) {

        System.out.println("\nName: " + user.getName() +
                "\nLast name: " + user.getLastname() +
                "\nPersonal Code: " + user.getPersonalCode());
    }
}
