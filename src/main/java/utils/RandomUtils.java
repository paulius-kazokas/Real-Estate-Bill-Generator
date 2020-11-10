package utils;

import java.util.Random;

import static config.SystemConstants.*;

public class RandomUtils {

    static Random random = new Random();

    public static double randomUtilityUnitPriceGenerator() {
        return Double.parseDouble(UPC_DECIMAL_FORMATTER
                .format(UTILITY_PRICE_MIN + (UTILITY_PRICE_MAX - UTILITY_PRICE_MIN) * random.nextDouble()));
    }

    public static double randomPVMGenerator() {
        return Double.parseDouble(UPC_DECIMAL_FORMATTER
                .format(PVM_MIN + (PVM_MAX - PVM_MIN) * random.nextDouble()));
    }
}
