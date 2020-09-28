package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class Property {

    private User user;
    private int id;
    private String ownerPersonalCode;
    private String type;
    private String address;

}
