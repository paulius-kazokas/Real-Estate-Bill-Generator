package utility;

import config.SystemConstants;

import java.util.Random;

public class RandomGeneratorUtility {

    public String randomIndicatorGenerator() {
        Random random = new Random();
        return SystemConstants.UTC_DECIMAL_FORMATER
                .format(SystemConstants.INDICATOR_MIN + (SystemConstants.INDICATOR_MAX - SystemConstants.INDICATOR_MIN) * random.nextDouble());
    }

    public String randomPVMGenerator() {
        Random random = new Random();
        return SystemConstants.UTC_DECIMAL_FORMATER
                .format(SystemConstants.PVM_MIN + (SystemConstants.PVM_MAX - SystemConstants.PVM_MIN) * random.nextDouble());
    }
}
