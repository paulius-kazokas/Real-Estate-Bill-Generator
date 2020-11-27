package config;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SystemConstants {

    // General specifics
    public static final InputStream IN = System.in;
    public static final OutputStream OUT = System.out;

    public static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");
    public static final String UPC_DATETIME_FORMATTER = "yyyy-MM";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(UPC_DATETIME_FORMATTER);
    public static final String CURRENT_MONTH = LocalDate.now().toString(DateTimeFormat.forPattern(SystemConstants.UPC_DATETIME_FORMATTER));

    // Database specifics
    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/utc?useSSL=true";
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PASSWORD = "root";

    // Filename specifics
    public static final int FROM_LIMIT = 97; // letter 'a'
    public static final int TO_LIMIT = 122; // letter 'z'
    public static final int LIMIT_SIZE = 10;

    public static final String FILTER_TYPE = "_byFilter";
    public static final String CURRENT_MONTH_TYPE = "_byCurrentMonth";
    public static final String CUSTOM_TYPE = "_byCustom";

    // Security specifics
    public static final String SHA512 = "SHA-512";

}
