package entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String personalCode;

}
