package utility;

import org.apache.commons.lang3.ArrayUtils;

public class InputValidity {

    public static boolean checkArrayForFalseItemValue(String[] itemsArr) {
        return ArrayUtils.contains(itemsArr,null) ||
               ArrayUtils.contains(itemsArr, " ") ||
               ArrayUtils.contains(itemsArr, "");
    }

}
