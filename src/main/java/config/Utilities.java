package config;

import lombok.Getter;
import utils.RandomUtils;

@Getter
public enum Utilities {


    ELECTRICITY("Electricity", RandomUtils.randomUtilityUnitPriceGenerator(0.09d, 0.45d), RandomUtils.randomPVMGenerator(8.00d, 12.5d)),
    GAS("Gas", RandomUtils.randomUtilityUnitPriceGenerator(0.05d, 0.35d), RandomUtils.randomPVMGenerator(4.00d, 9.50d)),
    WATER("Water", RandomUtils.randomUtilityUnitPriceGenerator(0.10d, 2.45d), RandomUtils.randomPVMGenerator(3.15d, 7.15d)),
    HEAT("Heat", RandomUtils.randomUtilityUnitPriceGenerator(2.05d, 2.45d), RandomUtils.randomPVMGenerator(5.00d, 8.70d)),
    OTHER("Other", RandomUtils.randomUtilityUnitPriceGenerator(0.35d, 2.45d), RandomUtils.randomPVMGenerator(3.00d, 5.55d)),
    EMPTY("", 0.00d, 0.00d);

    private String utilityName;
    private double unitPrice;
    private double utilityPvm;

    Utilities(String utility, double unitPrice, double utilityPvm) {
        this.utilityName = utility;
        this.unitPrice = unitPrice;
        this.utilityPvm = utilityPvm;
    }

}
