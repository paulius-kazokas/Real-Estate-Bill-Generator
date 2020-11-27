package utils;

import java.util.Random;

import static config.SystemConstants.*;

public class RandomUtils {

    static Random random = new Random();

    public static double randomUtilityUnitPriceGenerator(double unitPriceMin, double unitPriceMax) {
        return Double.parseDouble(DECIMAL_FORMATTER
                .format(unitPriceMin + (unitPriceMax - unitPriceMin) * random.nextDouble()));
    }

    public static double randomPVMGenerator(double utilityPvmMin, double utilityPvmMax) {
        return Double.parseDouble(DECIMAL_FORMATTER
                .format(utilityPvmMin + (utilityPvmMax - utilityPvmMin) * random.nextDouble()));
    }

    public static String uniqueFilenameGenerator() {
        return random.ints(FROM_LIMIT, TO_LIMIT + 1)
                .limit(LIMIT_SIZE)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
