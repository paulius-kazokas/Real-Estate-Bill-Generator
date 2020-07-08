package utility;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for InputVadilityTest class")
public class InputVadilityTest {

    InputVadility iv;

    @BeforeEach
    public void setup() {
        iv = new InputVadility();
    }

    @Order(1)
    @Test
    @DisplayName("When passing null")
    public void test1() {
        assertTrue(iv.checkArrayForFalseItemValue(ArrayUtils.toArray(null, "item1", "item2")));
    }

    @Order(2)
    @Test
    @DisplayName("When passing whitespace")
    public void test2() {
        assertTrue(iv.checkArrayForFalseItemValue(ArrayUtils.toArray(" ", "item1", "item2")));
    }

    @Order(3)
    @Test
    @DisplayName("When passing empty string")
    public void test3() {
        assertTrue(iv.checkArrayForFalseItemValue(ArrayUtils.toArray("", "item1", "item2")));
    }
}
