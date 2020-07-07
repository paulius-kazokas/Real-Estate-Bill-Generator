package database;

import config.DatabaseConfig;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for DatabaseActionsTest class")
public class DatabaseActionsTest {

    DatabaseConfig dc;
    DatabaseActions da;

    @BeforeEach
    public void setup() {
        dc = new DatabaseConfig();
        da = new DatabaseActions(dc);
    }

    @Nested
    @DisplayName("Checking if user exists")
    public class CheckIfUserExists {

        @Order(1)
        @Test
        @DisplayName("User exists")
        public void test1() throws SQLException {

            assertEquals("123", da.checkIfUserExists("123"));
        }

        @Order(2)
        @Test
        @DisplayName("User doesn't exist")
        public void test2() throws SQLException {
            assertEquals(null, da.checkIfUserExists("7894123asd198FSRW16316ZZX5fa16"));
        }

        @Order(3)
        @Test
        @DisplayName("Checking with empty string")
        public void test3() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfUserExists(""));
        }

        @Order(4)
        @Test
        @DisplayName("Checking with whitespace")
        public void test31() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfUserExists(" "));
        }

        @Order(5)
        @Test
        @DisplayName("Checking with null")
        public void test4() {
            assertThrows(IllegalArgumentException.class, () ->  da.checkIfUserExists(null));
        }
    }

    @Nested
    @DisplayName("Checking if password matches for user")
    public class CheckIfPasswordMatches {

        @Order(5)
        @Test
        @DisplayName("Password matches")
        public void test5() throws SQLException {
            assertTrue(da.checkIfPasswordMatches("123", "456"));
        }

        @Order(6)
        @Test
        @DisplayName("Password doesn't match")
        public void test6() throws SQLException {
            assertFalse(da.checkIfPasswordMatches("123", "123"));
        }

        @Order(7)
        @Test
        @DisplayName("Password is empty string")
        public void test7() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", ""));
        }

        @Order(8)
        @Test
        @DisplayName("Password is whitespace")
        public void test8() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", " "));
        }

        @Order(8)
        @Test
        @DisplayName("Password is null")
        public void test82() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", null));
        }
    }

    @Nested
    @DisplayName("Checking if registered user")
    public class RegisterNewUserTest {

        String testUsername = "testuser";

        @Order(9)
        @Test
        @DisplayName("Registering new user")
        public void test9() throws SQLException {
            da.registerNewUser(testUsername, "testpwd", "'testname'", "'testlastname'", "'test@mail.com'", "'123456789'");
            assertEquals(testUsername, da.checkIfUserExists(testUsername));
            da.deleteUserByUsername(testUsername);
        }

        @Order(10)
        @Test
        @DisplayName("Registering new user with null")
        public void test10() {
            assertThrows(IllegalArgumentException.class, () -> da.registerNewUser(null, "testpwd", "testname", "testlastname", "test@mail.com", "123456789"));
        }

        @Order(11)
        @Test
        @DisplayName("Registering new user with empty string")
        public void test11() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.registerNewUser("", "", "", "", "", ""));
        }

        @Order(12)
        @Test
        @DisplayName("Registering new user with empty string")
        public void test12() throws SQLException {
            assertThrows(IllegalArgumentException.class, () -> da.registerNewUser(" ", " ", " ", " ", " ", " "));
        }
    }
}
