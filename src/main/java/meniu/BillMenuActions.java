package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import entities.Utility;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static config.SystemConstants.*;

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

        String primaryChoice = "not assigned";

        while (!primaryChoice.equals("0")) {
            OUT.write("""

                    Urban Taxes Calculator

                    1.Check existing bills
                    2.Generate custom bill
                    0.Back

                    Choice: """.getBytes());
            primaryChoice = scanner.nextLine();

            if (!primaryChoice.isBlank()) {
                switch (primaryChoice) {
                    case "0" -> {
                        return;
                    }
                    // generate bill
                    case "2" -> customFilterBill(user);
                    default -> OUT.write("Unexpected action".getBytes());
                }
            }
        }
    }

    private JSONObject billBase(User user) {

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
    private List<Property> requestProperties() throws SQLException {

        // choose property/ies
        Set<Property> userProperties = propertyRepository.getPropertiesByUser(user);
        List<String> propertyAddresses = userProperties.stream().map(Property::getAddress).collect(Collectors.toList());
        OUT.write("""

                Select address/es:
                """.getBytes());
        AtomicInteger index = new AtomicInteger();
        index.set(1);
        propertyAddresses.forEach(address -> System.out.println(index.getAndIncrement() + ". " + address));
        String userInput = scanner.nextLine();

        return retrieveProperties(propertyAddresses, userInput);
    }

    private List<Property> retrieveProperties(List<String> addresses, String userInput) throws SQLException {

        List<Property> resultProperties = new ArrayList<>();
        // parse

        String[] addressess = userInput.split(",");
        System.out.println(addressess.length);
        if (addressess.length > 1) {
            for (String a : addressess) {
                String address = addresses.get(Integer.parseInt(a)-1);
                Property property = propertyRepository.getPropertyByAddress(address);
                property.setUser(user);
                resultProperties.add(property);
            }
        } else if (addressess.length == 1) {
            String address = addresses.get(Integer.parseInt(userInput) - 1);
            Property property = propertyRepository.getPropertyByAddress(address);
            property.setUser(user);
            resultProperties.add(property);
        }

        return resultProperties;
    }

    @SneakyThrows(IOException.class)
    private List<Utility> requestUtilities(Property property) throws SQLException {

        OUT.write(String.format("""

                Select utility/ies for %s:
                """, property.getAddress()).getBytes());
        AtomicInteger utilityIndex = new AtomicInteger();
        UTILITIES.forEach(utility -> System.out.println(utilityIndex.getAndIncrement() + ". " + utility));
        String utilityChoice = scanner.nextLine();

        return retrieveUtilities(utilityChoice);
    }

    private List<Utility> retrieveUtilities(String userInput) throws SQLException {

        List<Utility> resultUtilities = new ArrayList<>();

        List<String> utilities = UTILITIES.stream().filter(utility -> !utility.equals("ALL")).collect(Collectors.toList());
        String[] utilityChoiceUtilities = userInput.split(",");
        // all utilities
        if (utilityChoiceUtilities.length == 1 && Integer.valueOf(userInput) == utilities.size()) {
            for (String utilityName : utilities) {
                resultUtilities.add(utilityRepository.getUtility(utilityName));
            }
            // specific utilities
        } else if (utilityChoiceUtilities.length > 1) {
            for (String u : utilityChoiceUtilities) {
                String utilityName = utilities.get(Integer.parseInt(u) - 1);
                resultUtilities.add(utilityRepository.getUtility(utilityName));
            }
        }

        return resultUtilities;
    }

    @SneakyThrows(IOException.class)
    private List<String> requestDates(Property property, Utility utility) throws SQLException {
        OUT.write(String.format("""

                Select date/s for %s:
                """, property.getAddress()).getBytes());
        AtomicInteger dateIndex = new AtomicInteger();
        dateIndex.set(1);
        List<String> dates = indicatorRepository.getIndicatorDatesByPropertyAndUtility(property, utility);
        dates.forEach(date -> System.out.println(dateIndex.getAndIncrement() + ". " + date));
        String dateChoice = scanner.nextLine();

        return retrieveDates(dates, dateChoice);
    }

    private List<String> retrieveDates(List<String> dates, String dateChoice) {

        List<String> resultDates = new ArrayList<>();

        String[] datesArr = dateChoice.split(",");
        if (datesArr.length > 1) {
            Arrays.stream(datesArr).forEach(date -> resultDates.add(dates.get(Integer.parseInt(dateChoice) - 1)));
        } else if (datesArr.length == 1) {
            resultDates.add(dates.get(0));
        }

        return resultDates;
    }

    @SneakyThrows(IOException.class)
    private void customFilterBill(User user) throws SQLException {

        JSONObject bill = billBase(user);
        JSONArray allReportData = new JSONArray();

        List<Utility> utilities = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        // get properties
        List<Property> properties = requestProperties();

        // get utilities
        for (Property property : properties) {
            OUT.write(String.format("Utilities for %s", property.getAddress()).getBytes());
            utilities = requestUtilities(property);
        }

        // get dates
        for (Property property : properties) {
            for (Utility utility : utilities) {
                OUT.write(String.format("Dates for %s (%s)", property.getAddress(), utility.getName()).getBytes());
                dates = requestDates(property, utility);
            }
        }

        // get indicators
        for (Property property : properties) {
            JSONObject generatedData = new JSONObject();
            double grandTotal = 0.00d;
            generatedData.put("address", property.getAddress());
            for (Utility utility : utilities) {
                generatedData.put("utility", utility.getName());
                for (String date : dates) {
                    Indicator indicator = indicatorRepository.getIndicatorsByPropertyUtiltyAndDate(property, utility, date);

                    int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
                    double pvm = RandomUtils.randomPVMGenerator();
                    double SubTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(amount * RandomUtils.randomUtilityUnitPriceGenerator())));
                    double PvmTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format((pvm * SubTotal) / 100.00d)));
                    double IndicatorTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(Double.sum(SubTotal, PvmTotal))));

                    generatedData.put("date", date);
                    generatedData.put("indicator_amount", amount);
                    generatedData.put("sub_total", SubTotal);
                    generatedData.put("pvm_total", PvmTotal);
                    generatedData.put("total_indicator", IndicatorTotal);
                    grandTotal += IndicatorTotal;
                }
            }
            allReportData.put(generatedData);
            bill.put("total", Double.parseDouble(DECIMAL_FORMATTER.format(grandTotal)));
        }

        bill.put("report", allReportData);

        billRepository.saveBill(user, "in progress", bill);

        exportBill(bill);

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
