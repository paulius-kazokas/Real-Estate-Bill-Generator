import config.SystemConstants;
import data.DataGenerator;
import meniu.Meniu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DataGenerator dg = new DataGenerator();

        // utility unit price generation
        dg.generateUtilitiesUnitPriceReport(SystemConstants.BILL_DESTINATION_PATH, SystemConstants.UTC_CURRENT_MONTH_BILL_DATE);
        dg.generateUserMonthlyBillReport("456");

        Meniu.init();
    }
}
