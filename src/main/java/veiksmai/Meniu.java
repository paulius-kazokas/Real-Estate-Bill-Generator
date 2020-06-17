package veiksmai;

public class Meniu {
    // ToDo: replace sysout to logger

    static {
        // receive something here
    }

    public static void init() {
        pagrindinisMeniu();
    }

    private static void pagrindinisMeniu() {
        System.out.print("Mokesčių mokėjimo sistema\n\n" +
                "1.Prisijungimas\n" +
                "2.Registracija\n" +
                "3.Išeiti");
    }

    private static void prisijungimoMeniu() {
        System.out.print("Prisijungimo vardas:");
        // ..
        System.out.print("Įveskite slaptažodį:");
        // ..
    }

    private static void registracijosMeniu() {
        System.out.print("Įveskite vartotojo vardą:");

        System.out.print("Įveskite slaptažodį:");

        System.out.print("Įveskite asmens kodą:");
    }

    private static void pagrindinisVartotojoMeniu() {
        System.out.print("Pasirinkimai:\n" +
                "1.Peržiūrėti sąskaitą\n" +
                "2.Peržiūrėti paslaugą\n" +
                "3.Pežiūrėti vartotojo paskyrą\n" +
                "4.");
    }
}
