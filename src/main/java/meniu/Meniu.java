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
        pagrindinisMeniu();
    }

    public static void pagrindinisMeniu() throws SQLException {
        LOGGER.info("\nMokesčių mokėjimo sistema\n\n" +
                "1.Prisijungimas\n" +
                "2.Registracija\n" +
                "0.Išeiti\n");
        ManiuActions.mainMenuActions();
    }

    public static void pagrindinisPaskyrosMeniu() {
        LOGGER.info("\n--------------\n" +
                "Pasirinkimai:\n" +
                "1.Peržiūrėti sąskaitą\n" +
                "2.Peržiūrėti paslaugą\n" +
                "3.Pežiūrėti vartotojo paskyrą\n" +
                "0.");
    }

    private static void saskaitosMeniu() {

    }

    private static void paslauguMeniu() {

    }

    private static void vartotojoMeniu() {

    }

}
