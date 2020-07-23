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
    @DisplayName("Successfull data encryption/decryption")
    public void test1() {

        String testData = "test1";

        // encryption
        String encryptedValue = su.sha512Hash("test1");
        System.out.println(encryptedValue);
        assertFalse(encryptedValue.isBlank());
        assertEquals(24, encryptedValue.length());

        // decryption
        String decryptedValue = su.decrypt(encryptedValue);
        assertFalse(decryptedValue.isBlank());
        assertEquals(testData.length(), decryptedValue.length());
    }

}
