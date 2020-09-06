package playground;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Menus {

    private InputStream is = System.in;
    private OutputStream os = System.out;

    public static void main(String[] args) throws IOException {
        Menus m = new Menus();
        m.actions();
    }

    void actions()  throws IOException {
        os.write("playground.Menus".getBytes());
        MenuOne mo = new MenuOne(is, os);
        mo.actions();
    }
}

class MenuOne {

    private InputStream is;
    private OutputStream os;

    public MenuOne(InputStream is, OutputStream os) { // paduoti taip pat sarasa ir funkcijas (to investigate)1
        this.is = is;
        this.os = os;
    }

    void actions() throws IOException {
        String choice = "x";
        while(!choice.equals("0")) {
            Scanner scanner = new Scanner(is);
            os.write("\n1.call menu two\n2. menuOne2\n0.Log out".getBytes());
            choice = scanner.nextLine();
            os.write(choice.getBytes());
            if (choice.equals("1")) {
                MenuTwo mt = new MenuTwo(is, os);
                mt.actions();
            }
        }
        os.write("end menu one".getBytes());
    }
}

class MenuTwo {

    private InputStream is;
    private OutputStream os;

    public MenuTwo(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    void actions() throws IOException {
        String choice = "y";
        while(!choice.equals("0")) {
            Scanner scanner = new Scanner(is);
            os.write("\n1.menuTwo1\n2.menuTwo2\n0.back".getBytes());
            choice = scanner.nextLine();
            os.write(choice.getBytes());
        }
        os.write("end menu two".getBytes());
    }
}