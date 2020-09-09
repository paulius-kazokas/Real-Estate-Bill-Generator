package interfaces;

import entities.Bill;
import entities.Price;

public interface IPriceRepository {

    Price getPrice(int billId);

}
