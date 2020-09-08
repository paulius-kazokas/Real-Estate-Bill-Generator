package entities;

public class oUser {

    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String personalCode;

    public oUser() {
    }

    public oUser(String username, String name, String lastname, String email, String personalCode) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.personalCode = personalCode;
    }

    public oUser(String username, String password, String name, String lastname, String email, String personalCode) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.personalCode = personalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", personalCode=" + personalCode +
                '}';
    }
}
