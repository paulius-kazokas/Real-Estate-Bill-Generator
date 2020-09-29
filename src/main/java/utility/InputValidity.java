package utility;

import java.util.Arrays;

public class InputValidity {

    public static boolean validArray(String[] itemsArr) {
        return Arrays.stream(itemsArr).noneMatch(String::isBlank);
    }

}
