package meniu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Meniu {

    private static final Logger LOGGER = LoggerFactory.getLogger(Meniu.class);

    public static void init() throws SQLException {
        mainMenu();
    }

    public static void mainMenu() throws SQLException {
        System.out.print("\nUrban Taxes System\n\n" +
                "1.Sign up\n" +
                "2.Register\n" +
                "0.Exit\n");
        MeniuActions.mainMenuActions();
    }

    public static void mainAccountMenu(String loggedInUsername) {
        System.out.print("\n1.Check monthly bill\n" +
                "2.Check bill for specific utility\n" +
                "3.View account\n" +
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
