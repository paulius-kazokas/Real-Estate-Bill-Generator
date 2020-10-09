package entities;

import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

@Data(staticConstructor = "object")
@ToString
public class Indicator {

    private int id;
    private Utility utility;
    private DateTime date;
    private int monthStartAmount;
    private int monthEndAmount;

}
