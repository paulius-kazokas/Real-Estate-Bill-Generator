package database;

import config.DatabaseConfig;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for DatabaseActionsTest class")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseActionsTest {

    DatabaseActions da;

    @BeforeAll
    public void setup() throws SQLException {
        DatabaseConfig dc = new DatabaseConfig();
        da = new DatabaseActions(dc);
        da.registerNewUser("123", "456", "test", "test", "test@test.test", "123456789");
    }

    @AfterAll
    public void cleanup() throws SQLException {
        da.deleteUserByUsername("123");
    }

    @Nested
    @DisplayName("Checking if user exists")
    public class CheckIfUserExists {

        @Order(1)
        @Test
        @DisplayName("User exists")
        public void test1() throws SQLException {

            assertEquals("123", da.getUserByUsername("123"));
        }

        @Order(2)
        @Test
        @DisplayName("User doesn't exist")
        public void test2() throws SQLException {
            assertNull(null, da.getUserByUsername("7894123asd198FSRW16316ZZX5fa16"));
        }

        @Order(3)
        @Test
        @DisplayName("Checking with empty string")
        public void test3() {
            assertThrows(IllegalArgumentException.class, () -> da.getUserByUsername(""));
        }

        @Order(4)
        @Test
        @DisplayName("Checking with whitespace")
        public void test31(){
            assertThrows(IllegalArgumentException.class, () -> da.getUserByUsername(" "));
        }

        @Order(5)
        @Test
        @DisplayName("Checking with null")
        public void test4() {
            assertThrows(IllegalArgumentException.class, () ->  da.getUserByUsername(null));
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
        public void test7() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", ""));
        }

        @Order(8)
        @Test
        @DisplayName("Password is whitespace")
        public void test8() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", " "));
        }

        @Order(8)
        @Test
        @DisplayName("Password is null")
        public void test82() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatches("123", null));
        }
    }

    @Nested
    @DisplayName("Checking if registered user")
    public class RegisterNewUserTest {

        @Order(9)
        @Test
        @DisplayName("Registering new user")
        public void test9() throws SQLException {
            da.registerNewUser("testuser", "testpwd", "'testname'", "'testlastname'", "'test@mail.com'", "'123456789'");
            assertEquals("testuser", da.getUserByUsername("testuser"));
            da.deleteUserByUsername("testuser");
        }
    }

    @Nested
    @DisplayName("Checking if user deleted")
    public class DeleteUser {

        @Order(10)
        @Test
        @DisplayName("Deleting user")
        public void test10() throws SQLException {
            da.registerNewUser("testuser", "testpwd", "'testname'", "'testlastname'", "'test@mail.com'", "'123456789'");
            da.deleteUserByUsername("testuser");
            assertNull(null, da.getUserByUsername("testuser"));
        }
    }

}
