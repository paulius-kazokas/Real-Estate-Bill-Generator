package utils;

import java.util.Arrays;

public class InputUtils {

    public static boolean validArray(String[] array) {
        return Arrays.stream(array).allMatch(item -> item != null && !item.isBlank());
    }

}
