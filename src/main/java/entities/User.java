package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class User {

    private int id;
    private String personalCode;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;

}
