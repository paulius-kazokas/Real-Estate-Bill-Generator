package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import lombok.SneakyThrows;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static config.SystemConstants.*;
import static config.SystemConstants.UTC_CURRENT_MONTH_BILL_DATE;

public class BillMenuActions {

    private Scanner scanner = new Scanner(IN);

    private User user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private BillRepository billRepository;

    public BillMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, BillRepository billRepository, User user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.billRepository = billRepository;
        this.user = user;
    }

    @SneakyThrows(IOException.class)
    public void accountBillActions() throws SQLException {
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
