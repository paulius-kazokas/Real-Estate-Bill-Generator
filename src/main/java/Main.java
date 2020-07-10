import data.DataGenerator;
import meniu.Meniu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        // prideti patikrinima jeigu 2020-02 paskutine data duomenu reportu generavimas, o LocalDate.now() yra 2020-03 tada pergeneruoti (rollingMonthDataGeneration)
        DataGenerator dg = new DataGenerator();

        // utility unit price generation for rolling month
        dg.generateUtilitiesUnitPriceReport();
        // utility unit price pvm generation for rolling month
        dg.generateMontlyUtilityPVMs();

        Meniu.init();
    }
}
