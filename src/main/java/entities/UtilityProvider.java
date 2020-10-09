package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class UtilityProvider {

    private int id;
    private String name;
    private String additionalInformation;
}
