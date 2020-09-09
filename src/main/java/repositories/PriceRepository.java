package repositories;

import config.DatabaseConfig;
import entities.Bill;
import entities.Price;
import interfaces.IPriceRepository;

import static config.SystemConstants.*;

public class PriceRepository implements IPriceRepository {

    private DatabaseConfig databaseConfig;

    @Override
    public Price getPrice(int billId) {

        String query = String.format("SELECT %s FROM %s WHERE %s = %s",
                SELECT_ALL,
                UTC_PRICE_TABLE,
                UTC_PRICE_TABLE_ID, billId);

        // check github readme

        return null;
    }
}
