package config;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SystemConstants {

    // General specifics
    public static final DecimalFormat UTC_DECIMAL_FORMATER = new DecimalFormat("#.##");
    public static final String UPC_DATETIME_FORMATTER = "yyyy-MM";
    public static final String UTC_CURRENT_MONTH_BILL_DATE = LocalDate.now().toString(DateTimeFormat.forPattern(SystemConstants.UPC_DATETIME_FORMATTER));

    // Database specifics
    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/utc?useSSL=true";
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PASSWORD = "root";

    public static final String SELECT_ALL = "*";

    // Database: User
    public static final String UTC_USERS_TABLE = "utc.user";
    public static final String UTC_USERS_TABLE_ID = "id";
    public static final String UTC_USERS_TABLE_PERSONAL_CODE = "personalcode";
    public static final String UTC_USERS_TABLE_USERNAME = "username";
    public static final String UTC_USERS_TABLE_PASSWORD = "password";
    public static final String UTC_USERS_TABLE_NAME = "name";
    public static final String UTC_USERS_TABLE_LASTNAME = "lastname";
    public static final String UTC_USERS_TABLE_EMAIL = "email";

    // Database: Indicator
    public static final String UTC_INDICATORS_TABLE = "utc.indicator";
    public static final String UTC_INDICATORS_TABLE_ID = "id";
    public static final String UTC_INDICATORS_TABLE_PROPERTY_ID = "property_id";
    public static final String UTC_INDICATORS_TABLE_UTILITY_ID = "utility_id";
    public static final String UTC_INDICATORS_TABLE_DATE = "date";
    public static final String UTC_INDICATORS_TABLE_MONTH_START_AMOUNT = "month_start_amount";
    public static final String UTC_INDICATORS_TABLE_MONTH_END_AMOUNT = "month_end_amount";

    // Database: Property
    public static final String UTC_PROPERTY_TABLE = "utc.property";
    public static final String UTC_PROPERTY_TABLE_ID = "id";
    public static final String UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE = "ownderPersonalCode";
    public static final String UTC_PROPERTY_TABLE_TYPE = "type";
    public static final String UTC_PROPERTY_TABLE_ADDRESS = "address";

    // Database: Utility
    public static final String UTC_UTILITY_TABLE = "utc.utility";
    public static final String UTC_UTILITY_TABLE_ID = "id";
    public static final String UTC_UTILITY_TABLE_NAME = "name";

    // Database: Price
    public static final String UTC_PRICE_TABLE = "utc.price";
    public static final String UTC_PRICE_TABLE_ID = "id";
    public static final String UTC_PRICE_TABLE_CURRENCY = "currency";
    public static final String UTC_PRICE_TABLE_COURSE = "course";

    // Database: Bill
    public static final String UTC_BILL_TABLE = "utc.bill";
    public static final String UTC_BILL_TABLE_ID = "id";
    public static final String UTC_BILL_TABLE_INDICATOR_ID = "indicator_id";
    public static final String UTC_BILL_TABLE_PRICE = "price";

    // Bill specifics
    public static final String CURRENCY = "EUR";
    public static final String BILL_DESTINATION_PATH = "src/main/resources/data/";
    public static final String BILL_DESTINATION_TEST_PATH = "src/test/resources/data/";
    public static final int INDICATOR_MIN = 10;
    public static final int INDICATOR_MAX = 96;
    public static final double UTILITY_PRICE_MIN = 0.09d;
    public static final double UTILITY_PRICE_MAX = 0.64d;
    public static final double PVM_MIN = 0.05d;
    public static final double PVM_MAX = 0.15d;
    public static final List<String> UTILITIES = Arrays.asList("Heat", "Electricity", "Water", "Gas", "Trash");
    public static final String UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_UtilityPrices.json";
    public static final String UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES_PVM = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_PVM.json";

    // Security specifics
    public static final String SHA512 = "SHA-512";

}
