package interfaces;

import entities.Bill;
import entities.Indicator;

public interface IBillRepository {

    Bill getBill(Indicator indicator);


}
