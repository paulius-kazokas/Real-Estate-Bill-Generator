import data.DataGenerator;
import meniu.Meniu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DataGenerator dg = new DataGenerator();

        // utility unit price generation for rolling month
        dg.generateUtilitiesUnitPriceReport();

        Meniu.init();
    }
}
