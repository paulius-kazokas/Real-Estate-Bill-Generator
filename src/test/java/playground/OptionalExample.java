package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OptionalExample {

    class Person {

        private String name;
        private String lastname;
        private String contactNo;
        private String profession;
        private Address address;

        Person(String name, String lastname) {
            this.name = name;
            this.lastname = name;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        String getLastname() {
            return lastname;
        }

        void setLastname(String lastname) {
            this.lastname = lastname;
        }

        String getContactNo() {
            return contactNo;
        }

        void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        String getProfession() {
            return profession;
        }

        void setProfession(String profession) {
            this.profession = profession;
        }

        Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        void setAddress(Address address) {
            this.address = address;
        }

    }

    class Address {

        private Country country;

        Optional<Country> getCountry() {
            return Optional.ofNullable(country);
        }

        Address(Country country) {
            this.country = country;
        }
    }

    class Country {

        private String countryCode;

        Country(String countryCode) {
            this.countryCode = countryCode;
        }

        Optional<String> getCountryCode() {
            return Optional.ofNullable(countryCode);
        }
    }

    // CREATING OPTIONAL

    // Optional.empty()
    @Test
    void whenEmptyOptionalThenExpectNoSuchElementExceptionOnGet() {
        Optional<Person> optional = Optional.empty();

        assertThrows(NoSuchElementException.class, optional::get); // optional.get()
    }

    // Optional.of() null
    @Test
    void whenNullOptionalExpectNullPointerException() {
        Person person = null;

        assertThrows(NullPointerException.class, () -> Optional.of(person));
    }

    // Optional.of() non-null
    @Test
    void whenNonNullOptionalThenOk() {
        Person person = new Person("Johny", "Bravo");
        Optional<Person> user = Optional.of(person);

        assertEquals(user.get().getName(), "Johny");
    }

    // Optional.ofNullable()
    @Test
    void whenOfNullableExpectEither() {
        Person person = null;
        Optional<Person> optional = Optional.ofNullable(person);

        Assertions.assertEquals(optional, Optional.empty());
    }

    @Test
    void whenOptionIfPresentThenOk() {
        Person person = new Person("Johny", "Bravo");
        Optional<Person> optional = Optional.ofNullable(person);

        assertTrue(optional.isPresent());
    }
    // there can be OptionalInt, OptionalLong, OptionalDouble too


    // ACCESSING OPTIONAL

    // isPresent()
    @Test
    void whenNonEmptyObjectThenGet() {
        Person person = new Person("Somnath", "76543210");
        Optional<Person> optionalPerson = Optional.ofNullable(person);

        assertEquals(optionalPerson.isPresent(), true);
        assertEquals(optionalPerson.get(), person);
    }

    // ifPresent()
    @Test
    void whenOptionalIfPresentThenNotOk() {
        Optional<Person> optional = Optional.ofNullable(null);
        optional.ifPresent(p -> assertEquals(p.getName(), "John"));
    }

    // orElse() null
    @Test
    void whenEmptyObjectThenReturnDefault() {
        Person person1 = null;
        Person person2 = new Person("Somnath", "76543210");
        Person optionalPerson = Optional.ofNullable(person1).orElse(person2);

        assertEquals(optionalPerson, person2);
    }

    // orElse() non-null
    @Test
    void whenNonEmptyObjectThenOk() {
        Person person1 = new Person("John", "76543290");
        Person person2 = new Person("Somnath", "76543210");
        Person optionalPerson = Optional.ofNullable(person1).orElse(person2);

        assertEquals(optionalPerson, person1);
    }

    // orElseGet() non-null
    @Test
    void whenNonEmptyObjectThenOrElseGet() {
        Person person1 = new Person("John", "76543290");
        Person optionalPerson = Optional.ofNullable(person1).orElseGet(() -> new Person("Somnath", "76543210"));

        assertEquals(optionalPerson, person1);
    }
    // note: orElseGet() Supplier executed only when null

    // orElseGet() null
    @Test
    void whenEmptyObjectThenOrElseGet() {
        Person person1 = null;
        Person person2 = new Person("Somnath", "76543210");
        Person optionalPerson = Optional.ofNullable(person1).orElseGet(() -> person2);

        assertEquals(optionalPerson, person2);
    }

    // orElseThrow()
    @Test
    void whenEmptyObjectThenOrElseThrow() {
        Person person1 = null;

        assertThrows(RuntimeException.class, () -> Optional.ofNullable(person1).orElseThrow(RuntimeException::new));
    }

    // TRANSFORMING OPTIONAL

    // map() non-null
    @Test
    void whenOptionalAndMapThenOk() {
        Person person1 = new Person("John", "876543009");
        String result = Optional.ofNullable(person1).map(p -> p.getName().toUpperCase()).orElse("Unknown");

        assertEquals(result, "JOHN");
    }

    // map() null
    @Test
    void whenNullOptionalAndMapThenOk() {
        Person person1 = null;
        String result = Optional.ofNullable(person1).map(p -> p.getName().toUpperCase()).orElse("Unknown");

        assertEquals(result, "Unknown");
    }

    // flatMap()
    @Test
    void whenChainingThenOk() {
        Country newZealand = new Country("NZ");
        Address address = new Address(newZealand);
        Person person = new Person("Ross", "98700987");

        person.setAddress(address);

        String countryCode = Optional.of(person)
                .flatMap(Person::getAddress)
                .flatMap(Address::getCountry)
                .flatMap(Country::getCountryCode)
                .orElse("UnKnown");

        assertEquals(countryCode, newZealand.getCountryCode().get());
    }

    // flatMap() own example
    void whenownChainingThenOk() {
        Country lithuania = new Country("LTU");
        Address address = new Address(lithuania);
        Person person = new Person("Paulius", "Leonardas");

        person.setAddress(address);

        String countryCode = Optional.of(person)
                .flatMap(Person::getAddress)
                .flatMap(Address::getCountry)
                .flatMap(Country::getCountryCode)
                .orElse("Unknown");

        assertEquals(countryCode, lithuania.getCountryCode());
    }

    // JAVA 9 REQUIRED

    // or() ifPresentOrElse() stream()

    // or()
    @Test
    void whenUseOrThenOk() {
        Person person = null;
        Person optionalPerson = Optional.ofNullable(person).or(() -> Optional.of(new Person("name", "lastname"))).get();

        assertEquals(optionalPerson.getName(), "name");
    }

    // ifPresentOrElse()
    @Test
    void whenUseIfPresentOrElseThenOk() {
        Person person = null;
        Optional.ofNullable(person).ifPresentOrElse(
                p -> System.out.println(p.getName()),
                () -> System.out.println("Runnable"));
    }

    // isStream()
    @Test
    void whenOptionalStreamThenCollect() {
        Person person = new Person("Pablo", "De La Verte");
        List<String> name = Optional.ofNullable(person).stream().map(p -> p.getName()).collect(Collectors.toList());

        assertTrue(name.contains("Pablo"));
    }
}
