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

                    Bill Menu

                    1.Generate custom bill
                    0.Back

                    Choice: """.getBytes());
            primaryChoice = scanner.nextLine();

            if (!primaryChoice.isBlank()) {
                switch (primaryChoice) {
                    case "0" -> {
                        return;
                    }
                    // generate bill
                    case "1" -> customFilterBill(user);
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
    private List<Property> requestProperties(User user) throws SQLException {

        Set<Property> userProperties = propertyRepository.getPropertiesByUser(user);

        List<String> propertyAddresses = userProperties.stream().map(Property::getAddress).collect(Collectors.toList());
        OUT.write("""

                Select address/es:
                """.getBytes());
        AtomicInteger index = new AtomicInteger();
        index.set(1);
        propertyAddresses.forEach(address -> System.out.println(index.getAndIncrement() + ". " + address));
        OUT.write("or press '*' to select all properties\n".getBytes());
        String userInput = scanner.nextLine();
        // filterCommandLine += userInput;

        return retrieveProperties(propertyAddresses, userInput, user);
    }

    private List<Property> retrieveProperties(List<String> addresses, String userInput, User user) throws SQLException {

        if (userInput.equals("*")) {
            return new ArrayList<>(propertyRepository.getPropertiesByUser(user));
        }

        List<Property> resultProperties = new ArrayList<>();
        String[] addressess = userInput.split(",");

        if (addressess.length > 1) {
            for (String a : addressess) {
                String address = addresses.get(Integer.parseInt(a) - 1);
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

                Select utility/ies for '%s':
                """, property.getAddress()).getBytes());
        AtomicInteger utilityIndex = new AtomicInteger();
        utilityIndex.set(1);
        UTILITIES.forEach(utility -> System.out.println(utilityIndex.getAndIncrement() + ". " + utility));
        OUT.write("or press '*' to select all utilities\n".getBytes());
        String utilityChoice = scanner.nextLine();
        // filterCommandLine += utilityChoice;

        return retrieveUtilities(utilityChoice);
    }

    @SneakyThrows(IOException.class)
    private List<Utility> retrieveUtilities(String userInput) throws SQLException {

        List<Utility> resultUtilities = new ArrayList<>();

        String[] utilityChoiceUtilities = userInput.split(",");
        // all utilities
        if (userInput.equals("*")) {
            //if (utilityChoiceUtilities.length == 1 && Integer.parseInt(userInput) == utilities.size()) {
            for (String utilityName : UTILITIES) {
                resultUtilities.add(utilityRepository.getUtility(utilityName));
            }
            // specific utilities
        } else if (utilityChoiceUtilities.length > 1) {
            for (String u : utilityChoiceUtilities) {
                String utilityName = UTILITIES.get(Integer.parseInt(u));
                resultUtilities.add(utilityRepository.getUtility(utilityName));
            }
        } else if (Integer.parseInt(userInput) < utilityChoiceUtilities.length || Integer.parseInt(userInput) > utilityChoiceUtilities.length) {
            OUT.write(String.format("Invalid utility request '%s'", userInput).getBytes());
            return Collections.emptyList();
        }

        return resultUtilities;
    }

    @SneakyThrows(IOException.class)
    private List<String> requestDates(Property property, Utility utility) throws SQLException {

        OUT.write(String.format("""

                Select date/s for %s (%s):
                """, property.getAddress(), utility.getName()).getBytes());
        AtomicInteger dateIndex = new AtomicInteger();
        dateIndex.set(1);
        List<String> dates = indicatorRepository.getIndicatorDatesByPropertyAndUtility(property, utility);
        if (dates.isEmpty()) {
            OUT.write(String.format("No dates for: address - '%s'; utility - '%s'", property.getAddress(), utility.getName()).getBytes());
        } else {
            dates.forEach(date -> System.out.println(dateIndex.getAndIncrement() + ". " + date));
            String userInput = scanner.nextLine();
            // filterCommandLine += userInput;

            return retrieveDates(dates, userInput);
        }

        return Collections.emptyList();
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

    private void customFilterBill(User user) throws SQLException {

        JSONObject bill = billBase(user);
        JSONArray allReportData = new JSONArray();

        String filterCommandLine = "";
        double grandTotal = 0.00d;

        List<Property> properties = requestProperties(user);

        for (Property property : properties) {
            JSONObject innerPropertyData = new JSONObject();
            JSONArray generatedPropertyData = new JSONArray();
            innerPropertyData.put("address", property.getAddress());
            List<Utility> utilities = requestUtilities(property);
            double propertyGrandTotal = 0.00d;

            for (Utility utility : utilities) {
                JSONObject generatedUtilityData = new JSONObject();
                generatedUtilityData.put("utility", utility.getName());
                List<String> dates = requestDates(property, utility);

                for (String date : dates) {

                    Indicator indicator = indicatorRepository.getIndicatorsByPropertyUtiltyAndDate(property, utility, date);
                    int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
                    double pvm = RandomUtils.randomPVMGenerator();
                    double subTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(amount * RandomUtils.randomUtilityUnitPriceGenerator())));
                    double pvmTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format((pvm * subTotal) / 100.00d)));
                    double indicatorTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(Double.sum(subTotal, pvmTotal))));
                    propertyGrandTotal += indicatorTotal;
                    grandTotal += indicatorTotal;

                    generatedUtilityData.put("date", date);
                    generatedUtilityData.put("indicator_amount", amount);
                    generatedUtilityData.put("sub_total", subTotal);
                    generatedUtilityData.put("pvm_total", pvmTotal);
                    generatedUtilityData.put("price_total", indicatorTotal);

                    generatedPropertyData.put(generatedUtilityData);

                    //filterCommandLine += ";";
                }
            }
            innerPropertyData.put("property_total", Double.parseDouble(DECIMAL_FORMATTER.format(propertyGrandTotal)));
            generatedPropertyData.put(innerPropertyData);
            allReportData.put(generatedPropertyData);
            bill.put("total", Double.parseDouble(DECIMAL_FORMATTER.format(grandTotal)));
        }
        bill.put("report", allReportData);
        billRepository.saveBill(user, filterCommandLine, bill);
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
