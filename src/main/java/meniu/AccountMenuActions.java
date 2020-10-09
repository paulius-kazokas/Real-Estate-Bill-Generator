package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UtilityProviderRepository;
import repositories.UtilityRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static config.SystemConstants.OTHER_UTILITY;

@Slf4j
public class AccountMenuActions {

    private static final InputStream IN = System.in;
    private static final OutputStream OUT = System.out;
    private Scanner scanner = new Scanner(IN);

    private User user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private UtilityProviderRepository utilityProviderRepository;

    public AccountMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, UtilityProviderRepository utilityProviderRepository, User user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.utilityProviderRepository = utilityProviderRepository;
        this.user = user;
    }

    @SneakyThrows(IOException.class)
    public void accountMenuActions() {

        String choice = "not assigned";
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        if (!properties.isEmpty()) {
            while (!choice.equals("0")) {
                OUT.write("""

                        Urban Taxes Calculator

                        1.Check my properties
                        2.Check my indicators
                        3.Check my bills
                        4.Check my account info
                        0.Log out

                        Choice: """.getBytes());
                choice = scanner.nextLine();

                if (!choice.isBlank()) {
                    switch (choice) {
                        case "0" -> {
                            OUT.write("\n(logged out)".getBytes());
                            return;
                        }
                        case "1" -> checkUserProperties();
                        case "2" -> checkUserIndicators();
                        case "3" -> checkUserBills();
                        case "4" -> checkUserInfo();
                        default -> OUT.write("Unexpected action".getBytes());
                    }
                }
            }
        } else {
            OUT.write(String.format("\n'%s' doesn't have any properties available", user.getUsername()).getBytes());
        }
    }

    @SneakyThrows(IOException.class)
    public void checkUserProperties() {

        OUT.write("""

                Properties:
                """.getBytes());
        for (Property property : propertyRepository.getPropertiesByUser(user)) {
            OUT.write(String.format("%s (%s)\n", property.getAddress(), property.getType()).getBytes());
        }

    }

    public Map<Integer, String> propertyElementMap(List<String> elementList) {

        Map<Integer, String> elementMap = IntStream.range(0, elementList.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        elementList::get,
                        (k, v) -> k, LinkedHashMap::new
                ));

        elementMap.forEach((k, v) -> System.out.println(String.format("%s. %s", k, v)));

        return elementMap;
    }

    @SneakyThrows(IOException.class)
    public void checkUserIndicators() {

        // get properties
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        // list available type
        OUT.write("""

                Select type:
                """.getBytes());

        Map<Integer, String> types = propertyElementMap(properties.stream().map(Property::getType).distinct().collect(Collectors.toList()));

        String type = scanner.nextLine();
        String chosenType = types.get(Integer.parseInt(type));

        // prompt available addresses for type
        OUT.write("""

                Select address:
                """.getBytes());

        List<Property> chosenTypeProperties = properties.stream().filter(pr -> pr.getType().equals(chosenType)).collect(Collectors.toList());
        Map<Integer, String> addresses = propertyElementMap(chosenTypeProperties.stream().map(Property::getAddress).distinct().collect(Collectors.toList()));

        String address = scanner.nextLine();
        String chosenAddress = addresses.get(Integer.parseInt(address));

        // view indicators for selected address property
        OUT.write("""

                Indicators:
                """.getBytes());

        List<Indicator> indicators = indicatorRepository.getIndicatorsByProperty(chosenType, chosenAddress);

        //gauti tikrus indicatoriu id, o ne pagal mapo id, kad gauciau tikrus utility providerius
        Map<String, String> indicatorsReport = new HashMap<>();
        indicators.forEach(i -> {
            String rawUtilityName = utilityRepository.getUtility(i.getId()).getName();
            String utilityName = rawUtilityName.equals(OTHER_UTILITY) ? rawUtilityName + " (" + utilityProviderRepository.getUtilityProvider(i.getId()).getName() + ")" : rawUtilityName;

            indicatorsReport.put(utilityName, String.format("%s - %s", i.getMonthStartAmount(), i.getMonthEndAmount()));
        });

        indicatorsReport.forEach((utility, indicatorData) -> System.out.println(String.format("%s: %s", utility, indicatorData)));

    }

    @SneakyThrows(IOException.class)
    public void checkUserBills() {

        String choice = "not assigned";

        while (!choice.equals("0")) {
            OUT.write("""

                    My bills

                    1.By utility
                    2.By month
                    3.By month range
                    4.Custom
                    0.Log out

                    Choice: """.getBytes());

            choice = scanner.nextLine();

            if (!choice.isBlank()) {
                switch (choice) {
                    case "0" -> {
                        OUT.write("\n(logged out)".getBytes());
                        return;
                    }
                    case "1" -> billByUtility();
                    case "2" -> billByMonth();
                    case "3" -> billByMonthRange();
                    case "4" -> billByAddress();
                    case "5" -> billByYear();
                    case "6" -> billByCustom();
                    default -> OUT.write("Unexpected action".getBytes());
                }
            }
        }

    }

    private void billByUtility() {
    }

    private void billByMonth() {
    }

    private void billByMonthRange() {
    }

    private void billByAddress() {
    }

    private void billByYear() {
    }

    private void billByCustom() {
    }

    @SneakyThrows(IOException.class)
    public void checkUserInfo() {
        OUT.write(String.format("%nName: %s%nLastname: %s%nPersonal code: %s%n", user.getName(), user.getLastname(), user.getPersonalCode()).getBytes());
    }

}