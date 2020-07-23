package repositories;

import config.DatabaseConfig;
import entities.Property;
import entities.User;
import interfaces.PropertyInterface;
import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PropertyRepository implements PropertyInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    DatabaseConfig databaseConfig;

    public PropertyRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Property> getUserProperties(User user) {

        List<Property> properties = new ArrayList<>();

        String query = "SELECT id, type, address FROM utc.property WHERE ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Property property = new Property(user);
                property.setPropertyId(resultSet.getInt("id"));
                property.setOwnderPersonalCode(user.getPersonalCode());
                property.setType(resultSet.getString("type"));
                property.setAddress(resultSet.getString("address"));

                properties.add(property);
            }

            return !properties.isEmpty() ? properties : null;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

    @Override
    public boolean userHasProperties(List<Property> properties) {
        return properties.size() != 0;
    }

    @Override
    public Map<Integer, String> getUserPropertiesCount(MultiValuedMap<String, String> properties) {

        Map<Integer, String> propertiesCount = new HashMap<>();
        Set<String> propertyTypes = new HashSet<>(properties.keySet());

        for (String uniqueType : propertyTypes) {
            int typeCount = 0;
            for (String propertiesType : properties.keys()) {

                if (propertiesType.equals(uniqueType)) {
                    typeCount++;
                }
            }
            propertiesCount.put(typeCount, uniqueType);
        }

        return propertiesCount;
    }

    @Override
    public Integer getPropertyIdByPropertyAddress(String address) {

        String query = "SELECT id FROM utc.property WHERE address ='" + address + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return null;
    }

    @Override
    public List<String> getProprtyAddressByPropertyType(String propertyType) {

        List<String> addresses = new ArrayList<>();

        String query = "SELECT address FROM utc.property WHERE type = '" + propertyType + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                addresses.add(resultSet.getString("address"));
            }
            return addresses;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return Collections.emptyList();
    }
}
