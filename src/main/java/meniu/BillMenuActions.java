package meniu;

import entities.*;
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

                    1.Generate current month bill
                    2.Generate custom bill
                    0.Back

                    Choice: """.getBytes());
            primaryChoice = scanner.nextLine();

            if (!primaryChoice.isBlank()) {
                switch (primaryChoice) {
                    case "0" -> {
                        return;
                    }
                    case "1" -> currentMonthBill(user);
                    case "2" -> generateCustomBill(user);
                    default -> OUT.write("Unexpected action".getBytes());
                }
            }
        }
    }

    private JSONObject billBase(User user) {

        JSONObject bill = new JSONObject();
        bill.put("date", CURRENT_MONTH);

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
    private void currentMonthBill(User user) throws SQLException {

        JSONObject bill = billBase(user);
        JSONArray allReportData = new JSONArray();

        for (Property property : requestProperties(user)) {
            JSONObject generatedPropertySpecificData = new JSONObject();
            JSONArray generatedPropertyData = new JSONArray();
            double propertyGrandTotal = 0.00d;
            boolean isExportable = false;

            List<Indicator> indicators = indicatorRepository.getIndicators(property, CURRENT_MONTH);
            if (!indicators.isEmpty()) {
                isExportable = true;
                for (Indicator indicator : indicators) {
                    Utility utility = utilityRepository.getUtility(indicator.getId());

                    int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
                    double pvm = RandomUtils.randomPVMGenerator();
                    double subTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(amount * RandomUtils.randomUtilityUnitPriceGenerator())));
                    double pvmTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format((pvm * subTotal) / 100.00d)));
                    double indicatorTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(Double.sum(subTotal, pvmTotal))));
                    propertyGrandTotal += indicatorTotal;

                    JSONObject generatedIndicatorData = new JSONObject();
                    generatedIndicatorData.put("indicator_amount", amount);
                    generatedIndicatorData.put("sub_total", subTotal);
                    generatedIndicatorData.put("pvm_total", pvmTotal);
                    generatedIndicatorData.put("price_total", indicatorTotal);
                    generatedIndicatorData.put("utility", utility.getName());

                    generatedPropertyData.put(generatedIndicatorData);
                }
            } else {
                OUT.write(String.format("""
                        Export is not available. No indicator data found for %s

                        """, property.getAddress()).getBytes());
            }
            generatedPropertySpecificData.put("address", property.getAddress());
            generatedPropertySpecificData.put("date", CURRENT_MONTH);
            generatedPropertySpecificData.put("property_total", Double.parseDouble(DECIMAL_FORMATTER.format(propertyGrandTotal)));
            allReportData.put(generatedPropertySpecificData);
            allReportData.put(generatedPropertyData);

            if (isExportable) {
                bill.put("report", allReportData);
                billRepository.saveBill(user, "current month", bill);
                exportBill(bill);
            }
        }
    }

    @SneakyThrows(IOException.class)
    private void generateCustomBill(User user) throws SQLException {

        Map<Property, Map<Utility, String>> userChoiceData = new HashMap<>();
        StringBuilder filterCmd = new StringBuilder();

        for (Property property : requestProperties(user)) {
            StringBuilder propertyFilter = new StringBuilder();
            propertyFilter.append(property.getAddress());
            List<Utility> utilities = requestUtilities(property);

            Map<Utility, String> userChoiceUtilityData = new HashMap<>();

            for (Utility utility : utilities) {
                StringBuilder utilityFilter = new StringBuilder();
                utilityFilter.append(utility.getName());
                List<String> dates = requestDates(property, utility);

                for (String date : dates) {
                    userChoiceUtilityData.put(utility, date);
                    utilityFilter.append(date);
                }

                userChoiceData.put(property, userChoiceUtilityData);
                propertyFilter.append(utilityFilter);
            }

            filterCmd.append(propertyFilter);
        }

        String filter = filterCmd.toString();

        Bill dbBill = billRepository.getBill(filter);

        // extract existing bill by filter
        if (Objects.nonNull(dbBill)) {
            OUT.write("Specified filter detected, bill was previously generated".getBytes());
            exportBill(new JSONObject(dbBill.getBillJson()));

            // generate bill
        } else {
            JSONObject bill = billBase(user);
            JSONArray allReportData = new JSONArray();

            String filterCommandLine = "";
            double grandTotal = 0.00d;

            Set<Map.Entry<Property, Map<Utility, String>>> propertyEntries = userChoiceData.entrySet();
            for (Map.Entry<Property, Map<Utility, String>> propertyEntry : propertyEntries) {
                Property property = propertyEntry.getKey();
                Map<Utility, String> utilityData = propertyEntry.getValue();
                Set<Map.Entry<Utility, String>> utilityEntries = utilityData.entrySet();

                JSONArray generatedPropertyData = new JSONArray();
                double propertyGrandTotal = 0.00d;

                for (Map.Entry<Utility, String> utilityEntry : utilityEntries) {

                    Utility utility = utilityEntry.getKey();
                    String date = utilityEntry.getValue();

                    Indicator indicator = indicatorRepository.getIndicatorsByPropertyUtilityAndDate(property, utility, date);

                    int amount = indicator.getMonthEndAmount() - indicator.getMonthStartAmount();
                    double pvm = RandomUtils.randomPVMGenerator();
                    double subTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(amount * RandomUtils.randomUtilityUnitPriceGenerator())));
                    double pvmTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format((pvm * subTotal) / 100.00d)));
                    double indicatorTotal = Double.parseDouble(String.valueOf(DECIMAL_FORMATTER.format(Double.sum(subTotal, pvmTotal))));
                    propertyGrandTotal += indicatorTotal;
                    grandTotal += indicatorTotal;

                    JSONObject generatedUtilityData = new JSONObject();
                    generatedUtilityData.put("utility", utility.getName());
                    generatedUtilityData.put("date", date);
                    generatedUtilityData.put("indicator_amount", amount);
                    generatedUtilityData.put("sub_total", subTotal);
                    generatedUtilityData.put("pvm_total", pvmTotal);
                    generatedUtilityData.put("price_total", indicatorTotal);

                    generatedPropertyData.put(generatedUtilityData);

                }
                JSONObject innerPropertyData = new JSONObject();
                innerPropertyData.put("address", property.getAddress());
                innerPropertyData.put("property_total", Double.parseDouble(DECIMAL_FORMATTER.format(propertyGrandTotal)));

                generatedPropertyData.put(innerPropertyData);
                allReportData.put(generatedPropertyData);
                bill.put("total", Double.parseDouble(DECIMAL_FORMATTER.format(grandTotal)));

            }
            bill.put("report", allReportData);
            billRepository.saveBill(user, filterCommandLine, bill);
            exportBill(bill);

        }

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

        if (userInput.equals("*")) {
            return new ArrayList<>(userProperties);
        }

        return retrieveProperties(propertyAddresses, userInput, user);
    }

    private List<Property> retrieveProperties(List<String> addresses, String userInput, User user) throws SQLException {

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
            for (String utilityName : UTILITIES) {
                resultUtilities.add(utilityRepository.getUtility(utilityName));
            }
            // one utility
        } else if (utilityChoiceUtilities.length == 1) {
            String utilityName = UTILITIES.get(Integer.parseInt(userInput) - 1);
            resultUtilities.add(utilityRepository.getUtility(utilityName));
            // specific utilities
        } else if (utilityChoiceUtilities.length > 1) {
            for (String u : utilityChoiceUtilities) {
                String utilityName = UTILITIES.get(Integer.parseInt(u) - 1);
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

    @SneakyThrows(IOException.class)
    private void exportBill(JSONObject report) {

        OUT.write("""

                Would you like to export the bill?(y) """.getBytes());
        String export = scanner.nextLine();
        if (export.equals("y") || export.equals("Y")) {
            String reportPath = BILL_DESTINATION_PATH + CURRENT_MONTH + "_" + user.getPersonalCode() + ".json";
            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write(report.toString());
                writer.flush();
            }

            OUT.write(String.format("Report successfully exported to %s\n", reportPath).getBytes());
        }
    }

}
