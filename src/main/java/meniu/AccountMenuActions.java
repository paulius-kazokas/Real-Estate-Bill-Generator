package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import repositories.BillRepository;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UtilityRepository;
import utils.RandomUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static config.SystemConstants.*;

@Slf4j
public class AccountMenuActions {

    private Scanner scanner = new Scanner(IN);

    private User user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private BillRepository billRepository;

    public AccountMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, BillRepository billRepository, User user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.billRepository = billRepository;
        this.user = user;
    }

    @SneakyThrows(IOException.class)
    public void accountMenuActions() throws SQLException {

        String primaryChoice = "not assigned";
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        if (!properties.isEmpty()) {
            while (!primaryChoice.equals("0")) {
                OUT.write("""

                        Urban Taxes Calculator

                        1.Check my properties
                        2.Check my indicators
                        3.Check my bills
                        4.Check my account info
                        0.Log out

                        Choice: """.getBytes());
                primaryChoice = scanner.nextLine();

                if (!primaryChoice.isBlank()) {
                    switch (primaryChoice) {
                        case "0" -> {
                            OUT.write("\n(logged out)".getBytes());
                            return;
                        }
                        // check my properties
                        case "1" -> {
                            OUT.write("Properties:\n".getBytes());
                            properties.forEach(p -> {
                                try {
                                    OUT.write(String.format("%s (%s)\n", p.getAddress(), p.getPropertyType()).getBytes());
                                } catch (IOException ignored) {
                                }
                            });
                        }
                        // check my indicators
                        case "2" -> userIndicators().forEach((utility, indicatorData) -> System.out.println(String.format("%s: %s", utility, indicatorData)));
                        // check my bills
                        case "3" -> {
                            String secondaryChoice = "not assigned";

                            while (!secondaryChoice.equals("0")) {
                                OUT.write("""

                                        My bills

                                        1.By utility
                                        2.By month
                                        3.By month range
                                        4.By address
                                        5.By year
                                        6.Custom
                                        0.Back

                                        Choice: """.getBytes());

                                secondaryChoice = scanner.nextLine();

                                if (!secondaryChoice.isBlank()) {
                                    switch (secondaryChoice) {

                                        case "0" -> {
                                            return;
                                        }
                                        // filter by utility
                                        case "1" -> exportBill(billReportFilteredByUtility());
                                        // filter by month
                                        case "2" -> billReportFilteredByMonth();
                                        // filter by month range
                                        case "3" -> billReportFilteredByMonthRange();
                                        // filter by address
                                        case "4" -> exportBill(billReportFilteredByAddress());
                                        // filter by year
                                        case "5" -> billReportFilteredByYear();
                                        // custom filter
                                        case "6" -> customFilteredBillReport();
                                        default -> OUT.write("Unexpected action".getBytes());
                                    }
                                }
                            }
                        }
                        // check my account info
                        case "4" -> {
                            OUT.write(String.format("""
                                    %nName: %s
                                    Lastname: %s
                                    Personal code: %s
                                    """, user.getName(), user.getLastname(), user.getPersonalCode()).getBytes());
                            OUT.write("""
                                    Properties:
                                    """.getBytes());
                            for (Property p : properties) {
                                OUT.write(String.format("* %s (%s)\n", p.getAddress(), p.getPropertyType()).getBytes());
                            }
                        }
                        default -> OUT.write("Unexpected action".getBytes());
                    }
                }
            }
        } else {
            OUT.write(String.format("\n'%s' doesn't have any properties available", user.getUsername()).getBytes());
        }
    }

    public Map<Integer, String> elementToMap(List<String> elementList) {

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
    public Map<String, String> userIndicators() throws SQLException {

        // get properties
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        // list available type
        OUT.write("""

                Select type:
                """.getBytes());

        Map<Integer, String> types = elementToMap(properties.stream().map(Property::getPropertyType).distinct().collect(Collectors.toList()));

        String type = scanner.nextLine();
        String chosenType = types.get(Integer.parseInt(type));

        // prompt available addresses for type
        OUT.write("""

                Select address:
                """.getBytes());

        List<Property> chosenTypeProperties = properties.stream().filter(pr -> pr.getPropertyType().equals(chosenType)).collect(Collectors.toList());
        Map<Integer, String> addresses = elementToMap(chosenTypeProperties.stream().map(Property::getAddress).distinct().collect(Collectors.toList()));

        String address = scanner.nextLine();
        String chosenAddress = addresses.get(Integer.parseInt(address));

        // view indicators for selected address property
        OUT.write("""

                Indicators:
                """.getBytes());

        List<Indicator> indicators = indicatorRepository.getIndicatorsByProperty(chosenType, chosenAddress);

        //gauti tikrus indicatoriu id, o ne pagal mapo id, kad gauciau tikrus utility providerius
        Map<String, String> indicatorsMap = new HashMap<>();
        indicators.forEach(i -> {
            String utilityName = i.getUtility().getName();
            if (!utilityName.equals("Other")) {
                indicatorsMap.put(utilityName, String.format("%s - %s", i.getMonthStartAmount(), i.getMonthEndAmount()));
            }
        });

        return indicatorsMap;

    }

    private JSONObject billBase() {

        JSONObject bill = new JSONObject();
        bill.put("date", UTC_CURRENT_MONTH_BILL_DATE);

        JSONArray userData = new JSONArray();
        JSONObject ud = new JSONObject();
        ud.put("name", user.getName());
        ud.put("lastname", user.getLastname());
        ud.put("personal_code", user.getPersonalCode());
        userData.put(ud);

        bill.put("user_data", userData);

        return bill;
    }

    @SneakyThrows(IOException.class)
    private JSONObject billReportFilteredByUtility() throws SQLException {

        JSONObject bill = billBase();

        // list properties
        OUT.write("""

                Utilities:
                """.getBytes());
        AtomicInteger utilityIndex = new AtomicInteger();
        UTILITIES.forEach(utility -> System.out.println(utilityIndex.getAndIncrement() + ". " + utility));
        String utilityChoice = scanner.nextLine();
        String utility = UTILITIES.get(Integer.parseInt(utilityChoice));

        // get properties with chosen type with user personal_code
        Set<Property> properties = propertyRepository.getPropertiesByUtilityType(user, utility);
        List<String> propertyAddresses = properties.stream().map(Property::getAddress).collect(Collectors.toList());
        OUT.write("""

                Addresses:
                """.getBytes());
        AtomicInteger addressIndex = new AtomicInteger();
        propertyAddresses.forEach(address -> System.out.println(addressIndex.getAndIncrement() + ". " + address));
        String addressChoice = scanner.nextLine();
        String address = propertyAddresses.get(Integer.parseInt(addressChoice));
        Property property = propertyRepository.getPropertyByAddress(address);
        property.setUser(user);

        // print available dates for address with specific utility
        List<String> availableDatesForAddressWithChosenUtility = indicatorRepository.getIndicatorsByAddressAndUtilityName(user, property, utility);
        OUT.write("""

                Available dates:
                """.getBytes());
        AtomicInteger dateIndex = new AtomicInteger();
        availableDatesForAddressWithChosenUtility.forEach(date -> System.out.println(dateIndex.getAndIncrement() + ". " + date));
        String dateChoice = scanner.nextLine();
        String date = availableDatesForAddressWithChosenUtility.get(Integer.parseInt(dateChoice));

        // get prices and store to bill
        System.out.println(utility + " " + address + " " + date);
        int utilityId = utilityRepository.getUtility(utility).getId();
        Indicator indicator = indicatorRepository.getIndicator(property, utilityId, date);

        // calculations
        int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
        double pvm = RandomUtils.randomPVMGenerator();
        Double price = Double.parseDouble(String.valueOf((amount * RandomUtils.randomUtilityUnitPriceGenerator()) * pvm));
        Double formattedPrice = Double.parseDouble(UPC_DECIMAL_FORMATTER.format(Double.parseDouble(String.valueOf(price))));

        JSONArray utilityIndicatorPriceData = new JSONArray();

        JSONObject iup = new JSONObject();
        iup.put("utility", utility);
        iup.put("indicators_amount", amount);
        iup.put("for_date", date);
        iup.put("pvm", pvm);
        iup.put("price", formattedPrice);
        utilityIndicatorPriceData.put(iup);

        bill.put("utilities", utilityIndicatorPriceData);

        // save to the database
        billRepository.saveBill(property, bill);

        return bill;
    }

    private void billReportFilteredByMonth() {
    }

    private void billReportFilteredByMonthRange() {
    }

    private JSONObject billReportFilteredByAddress() throws SQLException {

        JSONObject bill = billBase();

        JSONArray utilityIndicatorPriceData = new JSONArray();

        // properties
        Set<Property> userProperties = propertyRepository.getPropertiesByUser(user);
        List<String> propertyAddresses = userProperties.stream().map(Property::getAddress).collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger();
        propertyAddresses.forEach(address -> System.out.println(index.getAndIncrement() + ". " + address));
        String addressChoice = scanner.nextLine();
        String address = propertyAddresses.get(Integer.parseInt(addressChoice));
        Property property = propertyRepository.getPropertyByAddress(address);
        property.setUser(user);

        // get property indicators
        List<Indicator> propertyIndicators = indicatorRepository.getIndicatorsByProperty(property.getPropertyType(), property.getAddress());
        // get indicator utilities
        List<String> utilities = propertyIndicators.stream().map(indicator -> indicator.getUtility().getName()).collect(Collectors.toList());
        // get monthStart/End per utility <Utility, <Amount, Price>>

        List<Double> prices = new ArrayList<>();

        utilities.forEach(utility -> propertyIndicators.forEach(indicator -> {
            if (indicator.getUtility().getName().equals(utility)) {

                int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
                double pvm = RandomUtils.randomPVMGenerator();
                Double price = Double.parseDouble(String.valueOf((amount * RandomUtils.randomUtilityUnitPriceGenerator()) * pvm));
                Double formattedPrice = Double.parseDouble(UPC_DECIMAL_FORMATTER.format(Double.parseDouble(String.valueOf(price))));

                JSONObject iup = new JSONObject();
                iup.put("utility", utility);
                iup.put("indicators_amount", amount);
                iup.put("pvm", pvm);
                iup.put("price", formattedPrice);
                utilityIndicatorPriceData.put(iup);

                prices.add(formattedPrice);
            }
        }));

        bill.put("utilities", utilityIndicatorPriceData);

        // total price
        double totalPrice = prices.stream().mapToDouble(utilityPrice -> utilityPrice).sum();
        bill.put("total", totalPrice);

        // save to the database
        billRepository.saveBill(property, bill);

        return bill;
    }

    private void billReportFilteredByYear() {
    }

    private void customFilteredBillReport() {
    }

    @SneakyThrows(IOException.class)
    private void exportBill(JSONObject report) {

        OUT.write("""

            Export bill?(y) """.getBytes());
        String export = scanner.nextLine();
        if (export.equals("y") || export.equals("Y")) {
            String reportPath = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_" + user.getPersonalCode() + ".json";
            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write(report.toString());
                writer.flush();
            }

            OUT.write(String.format("Report successfully exported to %s\n", reportPath).getBytes());
        }
    }

}