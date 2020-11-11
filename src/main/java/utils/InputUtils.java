package utils;

import java.util.Arrays;

public class InputUtils {

    public static boolean validArray(String[] array) {
        return Arrays.stream(array).allMatch(item -> item != null && !item.isBlank());
    }

    public static boolean validDatesArray(String[] array) {
        return Arrays.stream(array).allMatch(item ->
            item != null && !item.isBlank() && item.length() == 6 &&
            item.contains("-") && ( item.substring(0, 3).matches("[0-9]") && item.substring(5, 6).matches("[0-9]") )
        );
    }

}
