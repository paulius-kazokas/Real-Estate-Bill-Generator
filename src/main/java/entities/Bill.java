package entities;

import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

@Data(staticConstructor = "object")
@ToString
public class Bill {

    private int id;
    private Indicator indicator;
    private Property property;
    private User user;
    private DateTime date;
    private double pvm;
    private double amount;

}
