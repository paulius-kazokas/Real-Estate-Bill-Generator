package data;

import config.SystemConstants;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for DataGenerator class")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataGeneratorTest {

    DataGenerator dg;

    @BeforeAll
    public void setup() {
        dg = new DataGenerator();
    }

    @Nested
    @DisplayName("Checking if json bill generated")
    public class CheckIfJSONBillGenerates {

        @Order(1)
        @Test
        @DisplayName("Generating bill")
        public void test1() {
            dg.generateUtilitiesUnitPriceReport(SystemConstants.BILL_DESTINATION_TEST_PATH, SystemConstants.UTC_CURRENT_MONTH_BILL_DATE);
        }
    }
}
