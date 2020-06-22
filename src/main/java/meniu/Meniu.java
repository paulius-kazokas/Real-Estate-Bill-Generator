package meniu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Meniu {

    private static final Logger LOGGER = LoggerFactory.getLogger(Meniu.class);

    static {
        // receive something here
    }

    public static void init() throws SQLException {
        mainMenu();
    }

    public static void mainMenu() throws SQLException {
        LOGGER.info("\nUrban Taxes System\n\n" +
                "1.Sign up\n" +
                "2.Register\n" +
                "0.Exit\n");
        ManiuActions.mainMenuActions();
    }

    public static void mainAccountMenu() {
        LOGGER.info("\n1.Peržiūrėti sąskaitą\n" +
                "2.Peržiūrėti paslaugą\n" +
                "3.Pežiūrėti vartotojo paskyrą\n" +
                "0.Log out");
    }

    private static void billMenu() {
        // filtravimas pagal menesius, pagal paslauga ir sumos skaiciavimas
    }

    private static void utilitiesMenu() {
        // admin and utility role persons only:  statistika saskaitu pagal tam tikras paslaugas tam tikrais menesiais
        // utility role people - negali redaguoti savo paskyros, mato tik savo paslaugu statistikas ir gali jas filtruoti
    }

    private static void accountMenu() {
        // asmenines informacijos atvaizdavimas ir redagavimas
        // brainsotrm: norint pakeisti amwniniua duomenis i sistema, kur admin turi approvinti pakeitima
    }

}
