package repositories;

import config.DatabaseConfig;
import entities.Property;
import interfaces.IndicatorInterface;

import java.util.List;

public class IndicatorRepository implements IndicatorInterface {

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Property> getUserProperties(String username) {

        return null;
    }

}
