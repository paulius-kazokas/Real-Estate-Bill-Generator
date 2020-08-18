package meniu;

import entities.Property;
import entities.User;
import main.Menu;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UtilityRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AccountMenuActions {

    private User user;
    private LoginMenuActions loginMenuActions;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;

    public AccountMenuActions(LoginMenuActions loginMenuActions, PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, User user) {
        this.loginMenuActions = loginMenuActions;
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.user = user;
    }

    public void accountMenuActions(User user) {

        AccountMenuActions accountMenuActions = new AccountMenuActions(loginMenuActions, propertyRepository, indicatorRepository, utilityRepository, user);
        Menu menu = new Menu();

        Scanner scanner = new Scanner(System.in);

        List<Property> properties = propertyRepository.getPropertiesByUser(user);

        if (!properties.isEmpty()) {
            System.out.print("\nChoice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    System.out.println("\nlogged out..");
                    menu.mainMenu(loginMenuActions);
                    break;
                case "1":
                    // loggedInUserProperties
                    System.out.println("\nProperties:\n");
                    for (Property property : properties) {
                        System.out.println(property.getType() + " - " + property.getAddress());
                    }
                    break;
                case "2":
                    accountMenuActions.loggedInUserIndicators(scanner, user);
                    break;
                case "3":
                    // loggedInUserBillActions
                    System.out.println("to be implemented");
                    break;
                case "4":
                    // loggedInUserInformation
                    System.out.println("\nName: " + user.getName() +
                            "\nLast name: " + user.getLastname() +
                            "\nPersonal Code: " + user.getPersonalCode());
                    break;
                default:
                    System.out.println("AccountMenuActions.accountMenuActions() failed.");
                    menu.loggedInMenu(loginMenuActions, user);
                    break;
            }
        } else {
            System.out.println("\n\n'" + user.getUsername() + "' doesn't have any properties available");
            menu.mainMenu(loginMenuActions);
        }
        menu.loggedInMenu(loginMenuActions, user);

    }

    public void loggedInUserIndicators(Scanner scanner, User user) {

        System.out.println("\nProperty types:");

        Map<Integer, String> userPropertyTypes = propertyRepository.getUserPropertiesCount(user);
        for (Map.Entry<Integer, String> entry : userPropertyTypes.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        System.out.print("\nSelect available property type: ");
        String propertyTypeChoice = scanner.nextLine();

        String chosenPropertyType = userPropertyTypes.get(Integer.valueOf(propertyTypeChoice));

        System.out.println("\nAvailable addresses:");
        Map<Integer, String> chosenAddresses = new LinkedHashMap<>();
        int addressCount = 1;

        for (Property property : propertyRepository.getPropertiesByType(user, chosenPropertyType)) {
            String address = property.getAddress();
            System.out.println(addressCount + ". " + address);

            chosenAddresses.put(addressCount, address);
            addressCount++;
        }

        System.out.print("\nSelect address: ");
        String propertyAddressChoice = scanner.nextLine();
        String address = chosenAddresses.get(Integer.valueOf(propertyAddressChoice));

        Property propertyByAddress = propertyRepository.getPropertyByAddress(address);

        System.out.println("\nIndicators for " + address + ":");
        List<Integer> indicatorIds = indicatorRepository.getIndicatorIdsByPropertyId(propertyByAddress.getPropertyId());
        for (int indicatorId : indicatorIds) {
            int utilityId = indicatorRepository.getUtilityIdByIndicatorId(indicatorId);
            String utilityName = utilityRepository.getUtilityNameByUtilityId(utilityId);

            indicatorRepository.getIndicatorMonthStartEndAmountsByIndicatorId(indicatorId).forEach(amount -> System.out.println(utilityName + ": " + amount));
        }

    }

}