package meniu;

import entities.User;

import java.sql.SQLException;

public class Meniu {

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

    public static void loggedInMenu(User user) throws SQLException {
        System.out.print("Welcome, " + user.getUsername() +
                "\n\n1.Check my properites" +
                "\n0.Log out");
        MeniuActions.loggedInMenuActions(user);
    }
}
