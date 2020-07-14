package user;

import java.util.Map;

public class User {

    private String name;
    private String lastname;
    private String email;

    private int personalCode;

    private String city;

    private Map<Integer, String> realEstateList;

    public User(String name, String lastname, String email, int personalCode, String city, Map<Integer, String> realEstateList) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.personalCode = personalCode;
        this.city = city;
        this.realEstateList = realEstateList;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public int getPersonalCode() {
        return personalCode;
    }

    public String getCity() {
        return city;
    }

    public Map<Integer, String> getRealEstateList() {
        return realEstateList;
    }

    @Override
    public String toString() {
        return "Vartotojas{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", personalCode=" + personalCode +
                ", city='" + city + '\'' +
                ", realEstateList=" + realEstateList +
                '}';
    }
}
