package data;

import config.DatabaseConfig;
import database.DatabaseActions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.FileUtility;
import utility.RandomGeneratorUtility;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static config.SystemConstants.*;

public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    // menesiniu paslaugu rodikliu reportas
    public void generateUtilitiesUnitPriceReport() {

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        JSONObject utilityUnitPriceReport = new JSONObject();
        utilityUnitPriceReport.put("date", UTC_CURRENT_MONTH_BILL_DATE);

        JSONArray utilitiesArray = new JSONArray();
        for (String utility : UTILITIES) {
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);
            uJSON.put("unitPrice", rg.randomIndicatorGenerator());
            utilitiesArray.put(uJSON);
        }

        utilityUnitPriceReport.put("utilities", utilitiesArray);

        try (FileWriter file = new FileWriter(UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES)) {
            file.write(utilityUnitPriceReport.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public void generateMontlyUtilityPVMs() {

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        // to be continued

    }

    // vartotojo menesiniu paslaugu reportas
    public void generateUserMonthlyBillReport(String username) throws SQLException {

        RandomGeneratorUtility rg = new RandomGeneratorUtility();
        DatabaseConfig dc = new DatabaseConfig();
        DatabaseActions da = new DatabaseActions(dc);

        JSONObject userBill = new JSONObject();
        String userBillName = BILL_DESTINATION_PATH + username + ".json";

        userBill.put("billNumber", "123456789");
        userBill.put("userPersonalCode", da.getPersonalCodeByUsername(username));

        JSONArray userBillUtilities = new JSONArray();
        for (String utility : UTILITIES) {
            String randomAmount = rg.randomIndicatorGenerator();
            JSONObject uJSON = new JSONObject();
            uJSON.put("utility", utility);
            uJSON.put("amount", randomAmount);
            uJSON.put("currency", CURRENCY);
            String utilityTotal = UTC_DECIMAL_FORMATER.format(Double.parseDouble(getUtilityUnitAmount(utility, UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES)) * Double.parseDouble(randomAmount));
            uJSON.put("utilityTotal", utilityTotal);
            userBillUtilities.put(uJSON);
        }

        userBill.put("utilities", userBillUtilities);
        userBill.put("pvm", "cia pareina pvm");
        userBill.put("total", "cia gaunamos utilityTotal ir susumuojamos ir padauginamos su pvm"); // ideja: jeigu suma gaunasi xx:x4, tai parasyti funkcija, kuri suapvalina iki xx:x9

        try (FileWriter file = new FileWriter(userBillName)) {
            file.write(userBill.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public String getUtilityUnitAmount(String utility, String utilitiesUnitPriceReportPath) { // username ?

        String amount = null;
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
                    amount = util.getString("unitPrice");
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return amount;
    }
}
