package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class Property {

    private int id;
    private User user;
    private String propertyType;
    private String address;

}
