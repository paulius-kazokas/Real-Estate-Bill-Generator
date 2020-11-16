package entities;

import lombok.Data;
import lombok.ToString;

@Data(staticConstructor = "object")
@ToString
public class Bill {

    private int id;
    private User user;
    private String filteringCmd;
    private String billJson;

}
