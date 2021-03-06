package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class Utility {

    private Integer id;
    private UtilityProvider utilityProvider;
    private String name;
    private String comment;

}
