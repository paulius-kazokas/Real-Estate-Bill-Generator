package meniu;

import entities.lIndicator;
import entities.lProperty;
import entities.lUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UserRepository;
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

    private lUser user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private UserRepository userRepository;

    public AccountMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, UserRepository userRepository, lUser user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.utilityRepository = utilityRepository;
        this.user = user;
    }

    public void accountMenuActions() {

        try {
            String choice = "not assigned";
            List<lProperty> properties = propertyRepository.getPropertiesByUser(user);

            if (!properties.isEmpty()) {

                while (!choice.equals("0")) {
                    output.write("\n1.Check my properties\n2.Check my indicators\n3.Check my bills\n4.Check my account info\n0.Log out".getBytes());

                    output.write("\nChoice: ".getBytes());
                    choice = scanner.nextLine();

                    if (!choice.isBlank()) {
                        switch (choice) {
                            case "0":
                                output.write("\nlogged out".getBytes());
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

            for (lProperty property : propertyRepository.getPropertiesByType(user, chosenPropertyType)) {
                String address = property.getAddress();
                output.write(String.format("%s. %s%n", addressCount, address).getBytes());

                chosenAddresses.put(addressCount, address);
                addressCount++;
            }

            output.write("\nSelect address: ".getBytes());
            String propertyAddressChoice = scanner.nextLine();
            String address = chosenAddresses.get(Integer.valueOf(propertyAddressChoice));

            lProperty property = propertyRepository.getPropertyByAddress(address);
            System.out.println(property.toString());

            output.write(String.format("%nIndicators for %s:%n", address).getBytes());
            //List<Integer> indicatorIds = indicatorRepository.getIndicatorIdsByPropertyId(propertyByAddress.getPropertyId());

            System.out.println("property id: " + property.getId());

            // fix from here
            // should return list of indicators
            // enhance query - indicators by property id where user has same property id
            List<lIndicator> indicators = indicatorRepository.getIndicatorsByPropertyId(property.getId());
            System.out.println(indicators);

            indicators.forEach(indicator -> {

                int indicatorId = indicator.getId();
                int utilityId = indicatorRepository.getUtilityIdByIndicatorId(indicatorId);
                String utilityName = utilityRepository.getUtilityNameByUtilityId(utilityId);

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