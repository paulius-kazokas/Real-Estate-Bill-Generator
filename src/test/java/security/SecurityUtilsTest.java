package security;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for SecurityUtils class")
public class SecurityUtilsTest {

    SecurityUtils su;

    @BeforeEach
    public void setup() {
        su = new SecurityUtils();
    }

    @Order(1)
    @Test
    @DisplayName("Successful data hashing")
    public void test1() {

        String testData = "test1";

        // hashing
        String encryptedValue = su.sha512Hash("test1");
        assertFalse(encryptedValue.isBlank());
        assertEquals(128, encryptedValue.length());
    }

}
