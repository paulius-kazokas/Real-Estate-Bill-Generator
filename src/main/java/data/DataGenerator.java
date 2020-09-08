package data;

import config.DatabaseConfig;
import entities.lUser;
import entities.oUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepository;
import utility.FileUtility;
import utility.RandomGeneratorUtility;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.SystemConstants.*;

public class DataGenerator {

    DatabaseConfig dc;

    public DataGenerator(DatabaseConfig dc) {
        this.dc = dc;
    }
    // add additional check if with all prices and pvms dates allign with the same date (for rolling month)


    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);


    // TODO: perdaryti
    // menesiniu paslaugu rodikliu reportas
    public void generateUtilitiesUnitPriceReport() {

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        JSONObject utilityUnitPriceReport = new JSONObject();
        utilityUnitPriceReport.put("date", UTC_CURRENT_MONTH_BILL_DATE);

        JSONArray utilitiesArray = new JSONArray();
        for (String utility : UTILITIES) {
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);
            uJSON.put("unitPrice", rg.randomUtilityPriceGenerator());
            utilitiesArray.put(uJSON);
        }

        utilityUnitPriceReport.put("utilities", utilitiesArray);

        try (FileWriter file = new FileWriter(UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES)) {
            file.write(utilityUnitPriceReport.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    } // date?

    // menesiniu paslaugu kainu pvm reportas
    public void generateMontlyUtilityPVMs() {

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        JSONObject utilityUnitPricePVMReport = new JSONObject();
        utilityUnitPricePVMReport.put("date", UTC_CURRENT_MONTH_BILL_DATE);

        JSONArray utilitiesPVMArray = new JSONArray();
        for (String utility : UTILITIES) {
            JSONObject pvmJSON = new JSONObject();
            pvmJSON.put("utility", utility);
            pvmJSON.put("pvm", rg.randomPVMGenerator());
            utilitiesPVMArray.put(pvmJSON);
        }

        utilityUnitPricePVMReport.put("utilities_pvm", utilitiesPVMArray);

        try (FileWriter file = new FileWriter(UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES_PVM)) {
            file.write(utilityUnitPricePVMReport.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    } // date?

    // vartotojo menesiniu paslaugu reportas
    public void generateUserMonthlyBillReport(String username) { // date?

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        UserRepository ur = new UserRepository(dc);
        //oUser user = ur.getUserByUsername(username);
        lUser user = ur.getUserByUsername(username);

        String userBillName = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_" + username + ".json";
        Map<String, String> allUtilitiesTotal = new HashMap<>();
        List<Double> billUtilityTotalsWithPVM = new ArrayList<>();

        JSONObject userBill = new JSONObject();

        userBill.put("date", UTC_CURRENT_MONTH_BILL_DATE);
        userBill.put("bill_number", "123456789");
        userBill.put("user_personal_code", user.getPersonalCode());

        JSONArray userBillUtilities = new JSONArray();
        for (String utility : UTILITIES) {
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);

            double utilityPVM = getPVMForUtility(utility, UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES_PVM);
            uJSON.put("pvm", utilityPVM);

            int randomAmount = rg.randomIndicatorGenerator();
            uJSON.put("indicators", randomAmount);

            String utilityTotalWithoutPVM = UTC_DECIMAL_FORMATER.format((getUtilityUnitPrice(utility, UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES) * randomAmount));
            uJSON.put("utility_total_without_pvm", utilityTotalWithoutPVM);

            String utilityPVMPrice = UTC_DECIMAL_FORMATER.format(Double.parseDouble(utilityTotalWithoutPVM) - (Double.parseDouble(utilityTotalWithoutPVM) * utilityPVM));
            uJSON.put("pvm_price", utilityPVMPrice);

            String utilityPriceWithPVM = UTC_DECIMAL_FORMATER.format(Double.parseDouble(utilityTotalWithoutPVM) + Double.parseDouble(utilityPVMPrice));
            uJSON.put("utility_total_with_pvm", utilityPriceWithPVM);
            billUtilityTotalsWithPVM.add(Double.parseDouble(utilityPriceWithPVM));

            userBillUtilities.put(uJSON);
            allUtilitiesTotal.put(utility, utilityPriceWithPVM);
        }

        userBill.put("currency", CURRENCY); // ideja: einancio dienos valiutos kursas
        userBill.put("utilities", userBillUtilities);
        userBill.put("total", sumUtilityPrices(billUtilityTotalsWithPVM)); // ideja: jeigu suma gaunasi xx:x4, tai parasyti funkcija, kuri suapvalina iki xx:x9

        try (FileWriter file = new FileWriter(userBillName)) {
            file.write(userBill.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    // TODO: perdaryti getUtilityUnitAmount ir getPVMForUtility i viena bendra dinamini data extraction metoda

    // paslaugos kaina (nepriklauso nuo utility reporto isdestymo tvarkos)
    public Double getUtilityUnitPrice(String utility, String utilitiesUnitPriceReportPath) { // username ?

        FileUtility fu = new FileUtility();

        try {
            if (fu.checkFileExistence(utilitiesUnitPriceReportPath)) {
                throw new FileNotFoundException(utilitiesUnitPriceReportPath + " unit price report not found.");
            }

            String utilityPricesReportContent = new String((Files.readAllBytes(Paths.get(utilitiesUnitPriceReportPath))));
            JSONObject reportJSON = new JSONObject(utilityPricesReportContent);
            JSONArray utilitiesJSON = reportJSON.getJSONArray("utilities");

            for (int u = 0; u < utilitiesJSON.length(); u++) {
                JSONObject util = utilitiesJSON.getJSONObject(u);
                String utilitiesJSONUtility = util.getString("utility");
                if (utilitiesJSONUtility.equals(utility)) {
                    return util.getDouble("unitPrice");
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return null;
    } // date?

    // paslaugos pvm (nepriklauso nuo pvm reporto isdestymo tvarkos)
    public Double getPVMForUtility(String utility, String utilitiesUnitPricePVMReportPath) {

        FileUtility fu = new FileUtility();

        try {
            if (fu.checkFileExistence(utilitiesUnitPricePVMReportPath)) {
                throw new FileNotFoundException(utilitiesUnitPricePVMReportPath + " unit price pvm report not found.");
            }

            String utilityPricePVMsReportContent = new String((Files.readAllBytes(Paths.get(utilitiesUnitPricePVMReportPath))));
            JSONObject reportJSON = new JSONObject(utilityPricePVMsReportContent);
            JSONArray utilitiesJSON = reportJSON.getJSONArray("utilities_pvm");

            for (int u = 0; u < utilitiesJSON.length(); u++) {
                JSONObject util = utilitiesJSON.getJSONObject(u);
                String utilitiesJSONUtility = util.getString("utility");
                if (utilitiesJSONUtility.equals(utility)) {
                    return util.getDouble("pvm");
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        return null;
    } // date?

    // susumuoja kainas
    public Double sumUtilityPrices(List<Double> utilityPrices) {
        return utilityPrices.stream().mapToDouble(Double::doubleValue).sum();
    }

}
