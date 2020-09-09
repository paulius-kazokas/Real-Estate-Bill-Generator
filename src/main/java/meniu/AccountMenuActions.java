package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UtilityRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AccountMenuActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountMenuActions.class);

    private InputStream input = System.in;
    private OutputStream output = System.out;
    private Scanner scanner = new Scanner(input);

    private User user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;

    public AccountMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, User user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.user = user;
    }

    public void accountMenuActions() {

        try {
            String choice = "not assigned";
            List<Property> properties = propertyRepository.getPropertiesByUser(user);

            if (!properties.isEmpty()) {

                while (!choice.equals("0")) {
                    output.write("\n1.Check my properties\n2.Check my indicators\n3.Check my bills\n4.Check my account info\n0.Log out".getBytes());

                    output.write("\nChoice: ".getBytes());
                    choice = scanner.nextLine();

                    if (!choice.isBlank()) {
                        switch (choice) {
                            case "0":
                                output.write("\n(logged out)".getBytes());
                                return;
                            case "1":
                                checkUserProperties();
                                break;
                            case "2":
                                checkUserIndicators();
                                break;
                            case "3":
                                checkUserBills();
                                break;
                            case "4":
                                checkUserInfo();
                                break;
                            default:
                                output.write("Unexpected action".getBytes());
                                break;
                        }
                    }
                }
            } else {
                output.write(String.format("%n'%s' doesn't have any properties available", user.getUsername()).getBytes());
            }

        } catch (IOException io) {
            LOGGER.error(io.toString());
        }
    }

    public void checkUserProperties() {

        try {
            output.write("\nProperties:\n".getBytes());

            propertyRepository.getPropertiesByUser(user).forEach(property ->
            {
                try {
                    output.write(String.format("%s - %s%n", property.getType(), property.getAddress()).getBytes());
                } catch (IOException io) {
                    LOGGER.error(io.toString());
                }
            });
        } catch (IOException io) {
            LOGGER.error(io.toString());
        }

    }

    public void checkUserIndicators() {

        try {
            output.write("\nProperty types:\n".getBytes());
            Map<Integer, String> userPropertyTypes = propertyRepository.getUserPropertiesCount(user);

            userPropertyTypes.forEach((key, value) -> {
                try {
                    output.write(String.format("%s. %s%n", key, value).getBytes());
                } catch (IOException io) {
                    LOGGER.error(io.toString());
                }
            });

            output.write("\nSelect available property type: ".getBytes());
            String propertyTypeChoice = scanner.nextLine();

            String chosenPropertyType = userPropertyTypes.get(Integer.valueOf(propertyTypeChoice));

            output.write("\nAvailable addresses:\n".getBytes());
            Map<Integer, String> chosenAddresses = new LinkedHashMap<>();
            int addressCount = 1;

            for (Property property : propertyRepository.getPropertiesByType(user, chosenPropertyType)) {
                String address = property.getAddress();
                output.write(String.format("%s. %s%n", addressCount, address).getBytes());

                chosenAddresses.put(addressCount, address);
                addressCount++;
            }

            output.write("\nSelect address: ".getBytes());
            String propertyAddressChoice = scanner.nextLine();
            String address = chosenAddresses.get(Integer.valueOf(propertyAddressChoice));

            Property property = propertyRepository.getPropertyByAddress(address);
            output.write(String.format("%nIndicators for %s:%n", address).getBytes());

            List<Indicator> indicators = indicatorRepository.getIndicatorsByPropertyId(property.getId(), user);

            indicators.forEach(indicator -> {

                int indicatorId = indicator.getId();
                int utilityId = indicatorRepository.getUtilityIdByIndicatorId(indicatorId);
                String utilityName = utilityRepository.getUtility(utilityId).getUtilityName();

                indicatorRepository.getIndicatorMonthStartEndAmountsByIndicatorId(indicatorId).forEach(amount -> {
                    try {
                        output.write(String.format("%s: %s%n", utilityName, amount).getBytes());
                    } catch (IOException io) {
                        LOGGER.error(io.toString());
                    }
                });
            });

        } catch (IOException io) {
            LOGGER.error(io.toString());
        }

    }

    public void checkUserBills() {

        try {
            output.write("to be implemented".getBytes());

            String choice = "not assigned";

            while (!choice.equals("0")) {

            }

        } catch (IOException io) {
            LOGGER.error(io.toString());
        }

    }

    public void checkUserInfo() {

        try {
            output.write(String.format("%nName: %s%nLastname: %s%nPersonal code: %s%n",
                    user.getName(), user.getLastname(), user.getPersonalCode()).getBytes());
        } catch (IOException io) {
            LOGGER.error(io.toString());
        }

    }

}