package repositories;

import config.DatabaseConfig;
import entities.Property;
import entities.User;
import interfaces.IPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PropertyRepository implements IPropertyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    DatabaseConfig databaseConfig;

    public PropertyRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Property> getPropertiesByUser(User user) {

        List<Property> properties = new ArrayList<>();

        String query = "SELECT id, type, address FROM utc.property WHERE ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Property property = new Property(user);
                property.setPropertyId(resultSet.getInt("id"));
                property.setOwnerPersonalCode(user.getPersonalCode());
                property.setType(resultSet.getString("type"));
                property.setAddress(resultSet.getString("address"));

                properties.add(property);
            }

            return properties;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public Map<Integer, String> getUserPropertiesCount(User user) {

        String typeQuery = "SELECT DISTINCT(type) FROM utc.property WHERE ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement typeStatement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet typeResultSet = typeStatement.executeQuery(typeQuery)) {

            List<String> propertyTypes = new ArrayList<>();

            while (typeResultSet.next()) {
                propertyTypes.add(typeResultSet.getString("type"));
            }

            Map<Integer, String> propertiesCount = new HashMap<>();
            for (String type : propertyTypes) {

                String propertyQuery = "SELECT count(type) AS count FROM utc.property WHERE ownderPersonalCode = '" + user.getPersonalCode() + "' AND type = '" + type + "'";

                try (Statement countStatement = databaseConfig.connectionToDatabase().createStatement();
                     ResultSet countResultSet = countStatement.executeQuery(propertyQuery)) {

                    while (countResultSet.next()) {
                        propertiesCount.put(countResultSet.getInt("count"), type);
                    }
                }
            }

            return propertiesCount;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public Property getPropertyByAddress(String address) {

        String query = "SELECT * FROM utc.property WHERE address ='" + address + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {

                Property property = new Property();
                property.setPropertyId(resultSet.getInt("id"));
                property.setOwnerPersonalCode(resultSet.getString("ownderPersonalCode"));
                property.setType(resultSet.getString("type"));
                property.setAddress(resultSet.getString("address"));

                return property;
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public List<Property> getPropertiesByType(User user, String type) {

        String query = "SELECT * FROM utc.property WHERE type = '" + type + "' AND ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Property> properties = new ArrayList<>();
            while (resultSet.next()) {

                Property returnProperty = new Property();
                returnProperty.setPropertyId(resultSet.getInt("id"));
                returnProperty.setOwnerPersonalCode(resultSet.getString("ownderPersonalCode"));
                returnProperty.setType(resultSet.getString("type"));
                returnProperty.setAddress(resultSet.getString("address"));

                properties.add(returnProperty);
            }

            return properties;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

}
