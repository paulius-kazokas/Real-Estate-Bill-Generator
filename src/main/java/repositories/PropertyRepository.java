package repositories;

import config.DatabaseConfig;
import entities.User;
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

    public PropertyRepository(DatabaseConfig databaseConfig) { this.databaseConfig = databaseConfig; }

    @Override
    public Map<String, String> getUserProperties(User user) {

        Map<String, String> properties = new HashMap<>();

        String query = "SELECT utc.property.type, utc.property.address FROM utc.property " +
                "WHERE utc.property.ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                properties.put(resultSet.getString("utc.property.type"), resultSet.getString("utc.property.address"));
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return properties;
    }
}
