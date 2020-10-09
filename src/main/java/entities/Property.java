package entities;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data(staticConstructor = "object")
@ToString
public class Property {

    private int id;
    private List<Indicator> indicators;
    private User user;
    private String type;
    private String address;

}
