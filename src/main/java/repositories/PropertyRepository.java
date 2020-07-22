package repositories;

import config.DatabaseConfig;
import interfaces.PropertyInterface;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
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
    public MultiValuedMap<String, String> getUserProperties(String userPersonalCode) {

        MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();

        String query = "SELECT type, address FROM utc.property WHERE ownderPersonalCode = '" + userPersonalCode + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                properties.put(resultSet.getString("type"), resultSet.getString("address"));
            }
            return properties;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

    @Override
    public boolean userHasProperties(MultiValuedMap<String, String> properties) {
        return !MultiMapUtils.isEmpty(properties);
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
