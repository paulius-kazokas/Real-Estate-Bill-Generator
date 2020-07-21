package meniu;

import entities.User;

public class Meniu {

    public static void init() {
        mainMenu();
    }

    public static void mainMenu() {
        System.out.print("\nUrban Taxes System\n\n" +
                "1.Sign up\n" +
                "2.Register\n" +
                "0.Exit\n");
        MeniuActions.mainMenuActions();
    }

    public static void loggedInMenu(User user) {
        System.out.print("\n1.Check my properites" +
                "\n2.Check my indicators" +
                "\n3.Check my bills" + // printinami available saskaitos (history) - year+month
                "\n0.Log out");
        MeniuActions.loggedInMenuActions(user);
    }

}
