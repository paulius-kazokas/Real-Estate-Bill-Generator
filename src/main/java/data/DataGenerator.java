package data;

import config.DatabaseConfig;
import config.SystemConstants;
import database.DatabaseActions;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.RandomGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    // menesiniu paslaugu rodikliu reportas
    public void generateUtilitiesUnitPriceReport(String reportPath, String reportDate) {

        RandomGenerator rg = new RandomGenerator();
        JSONObject utilityUnitPriceReport = new JSONObject();
        utilityUnitPriceReport.put("date", reportDate);

        JSONArray utilitiesArray = new JSONArray();
        for (String utility : SystemConstants.UTILITIES) {
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);
            uJSON.put("unitPrice", rg.randomIndicatorGenerator());
            utilitiesArray.put(uJSON);
        }

        utilityUnitPriceReport.put("utilities", utilitiesArray);

        String filename = reportPath + reportDate + "_UtilityPrices.json";

        try (FileWriter file = new FileWriter(filename)) {
            file.write(utilityUnitPriceReport.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public void generateMontlyUtilityPVMs() {

        RandomGenerator rg = new RandomGenerator();

    }

    // vartotojo menesiniu paslaugu reportas
    public void generateUserMonthlyBillReport(String username) throws SQLException {

        RandomGenerator rg = new RandomGenerator();
        DatabaseConfig dc = new DatabaseConfig();
        DatabaseActions da = new DatabaseActions(dc);

        JSONObject userBill = new JSONObject();

        userBill.put("billNumber", "123456789");
        userBill.put("userPersonalCode", da.getPersonalCodeByUsername(username));

        JSONArray userBillUtilities = new JSONArray();
        for (String utility : SystemConstants.UTILITIES) {
            String randomAmount = rg.randomIndicatorGenerator();
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);
            uJSON.put("amount", randomAmount);
            uJSON.put("utilityTotal", Double.valueOf(getUtilityUnitAmount(utility)) * Double.valueOf(randomAmount));
        }

        userBill.put("utilities", userBillUtilities);
        userBill.put("pvm", "cia pareina pvm");
        userBill.put("total", "cia gaunamos utilityTotal ir susumuojamos ir padauginamos su pvm"); // ideja: jeigu suma gaunasi xx:x4, tai parasyti funkcija, kuri suapvalina iki xx:x9

        String userBillName = SystemConstants.BILL_DESTINATION_PATH + username + ".json";

        try (FileWriter file = new FileWriter(userBillName)) {
            file.write(userBill.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public String getUtilityUnitAmount(String utility) {

        String amount = null;
        String utilityPricesReportPath = SystemConstants.BILL_DESTINATION_PATH;
        try {
            String utilityPricesReportContent = new String((Files.readAllBytes(Paths.get(utilityPricesReportPath))));
            JSONObject reportJSON = new JSONObject(utilityPricesReportContent);
            JSONArray utilitiesJSON = reportJSON.getJSONArray("utilities");
            for (int u = 0; u < utilitiesJSON.length(); u++) {
                String utilitiesJSONUtility = utilitiesJSON.getString(u);
                if (utilitiesJSONUtility.equals(utility)) {
                    JSONObject correctUtilityAmount = reportJSON.getJSONObject(utilitiesJSONUtility);
                    amount = correctUtilityAmount.getString("unitPrice");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amount;
    }
}
