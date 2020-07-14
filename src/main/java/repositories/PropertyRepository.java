package repositories;

import config.DatabaseConfig;
import interfaces.PropertyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class PropertyRepository implements PropertyInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    DatabaseConfig databaseConfig;

    public PropertyRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Map<String, String> getUserProperties(String userPersonalCode) {

        Map<String, String> properties = new HashMap<>();

        String query = "SELECT type, address FROM utc.property WHERE ownderPersonalCode = '" + userPersonalCode + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                properties.put(resultSet.getString("type"), resultSet.getString("address"));
            }
            return properties;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }
}
