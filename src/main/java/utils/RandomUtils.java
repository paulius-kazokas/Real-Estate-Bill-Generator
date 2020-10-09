package utils;

import java.util.Random;

import static config.SystemConstants.*;

public class RandomUtils {

    Random random = new Random();

    public int randomIndicatorGenerator() {
        return random.nextInt(INDICATOR_MAX - INDICATOR_MIN);
    }

    public double randomUtilityPriceGenerator() {
        return Double.parseDouble(UTC_DECIMAL_FORMATER
                .format(UTILITY_PRICE_MIN + (UTILITY_PRICE_MAX - UTILITY_PRICE_MIN) * random.nextDouble()));
    }

    public double randomPVMGenerator() {
        return Double.parseDouble(UTC_DECIMAL_FORMATER
                .format(PVM_MIN + (PVM_MAX - PVM_MIN) * random.nextDouble()));
    }
}
