package repositories;

import config.DatabaseConfig;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Running tests for DatabaseActionsTest class")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    UserRepository da;

    @BeforeAll
    public void setup() {
        DatabaseConfig dc = new DatabaseConfig();
        da = new UserRepository(dc);
        da.registerNewUser("123", "456", "test", "test", "test@test.test", "123456789");
    }

    @AfterAll
    public void cleanup() {
        da.deleteUserByUsername("123");
    }

    @Nested
    @DisplayName("Checking if user exists")
    public class CheckIfUserExists {

        @Order(1)
        @Test
        @DisplayName("User exists")
        public void test1() {
            assertTrue(da.checkIfUserAlreadyExists("123"));
        }

        @Order(2)
        @Test
        @DisplayName("User doesn't exist")
        public void test2() {
            assertFalse(da.checkIfUserAlreadyExists("7894123asd198FSRW16316ZZX5fa16"));
        }

        @Order(3)
        @Test
        @DisplayName("Checking with empty string")
        public void test3() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfUserAlreadyExists(""));
        }

        @Order(4)
        @Test
        @DisplayName("Checking with whitespace")
        public void test31(){
            assertThrows(IllegalArgumentException.class, () -> da.checkIfUserAlreadyExists(" "));
        }

        @Order(5)
        @Test
        @DisplayName("Checking with null")
        public void test4() {
            assertThrows(IllegalArgumentException.class, () ->  da.checkIfUserAlreadyExists(null));
        }
    }

    @Nested
    @DisplayName("Checking if password matches for user")
    public class CheckIfPasswordMatches {

        @Order(5)
        @Test
        @DisplayName("Password matches")
        public void test5() {
            assertTrue(da.checkIfPasswordMatchesForUsername("123", "456"));
        }

        @Order(6)
        @Test
        @DisplayName("Password doesn't match")
        public void test6() {
            assertFalse(da.checkIfPasswordMatchesForUsername("123", "123"));
        }

        @Order(7)
        @Test
        @DisplayName("Password is empty string")
        public void test7() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatchesForUsername("123", ""));
        }

        @Order(8)
        @Test
        @DisplayName("Password is whitespace")
        public void test8() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatchesForUsername("123", " "));
        }

        @Order(8)
        @Test
        @DisplayName("Password is null")
        public void test82() {
            assertThrows(IllegalArgumentException.class, () -> da.checkIfPasswordMatchesForUsername("123", null));
        }
    }

    @Nested
    @DisplayName("Checking if registered user")
    public class RegisterNewUserTest {

        @Order(9)
        @Test
        @DisplayName("Registering new user")
        public void test9() {
            da.registerNewUser("testuser2", "testpwd", "testname", "testlastname", "test@mail.com", "123456789");
            assertTrue(da.checkIfUserAlreadyExists("testuser2"));
            da.deleteUserByUsername("testuser");
        }
    }

    @Nested
    @DisplayName("Checking if user deleted")
    public class DeleteUser {

        @Order(10)
        @Test
        @DisplayName("Deleting user")
        public void test10() {
            da.registerNewUser("testuser", "testpwd", "testname", "testlastname", "test@mail.com", "123456789");
            da.deleteUserByUsername("testuser");
            assertFalse(da.checkIfUserAlreadyExists("testuser"));
        }
    }

}
