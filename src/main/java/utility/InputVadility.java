package utility;

import org.apache.commons.lang3.ArrayUtils;

public class InputVadility {

    public boolean checkArrayForFalseValue(String[] itemsArr) {
        return ArrayUtils.contains(itemsArr,null) ||
               ArrayUtils.contains(itemsArr, " ") ||
               ArrayUtils.contains(itemsArr, "");
    }
}
