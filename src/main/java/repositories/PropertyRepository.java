package repositories;

import config.DatabaseConfig;
import interfaces.PropertyInterface;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
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

    @Override
    public boolean userHasProperties(MultiValuedMap<String, String> properties) {
        return !MultiMapUtils.isEmpty(properties);
    }

    @Override
    public Map<Integer, String> getUserPropertiesCount(MultiValuedMap<String, String> properties) {

        Map<Integer, String> propertiesCount = new HashMap<>();
        Set<String> propertyTypes = new HashSet<>(properties.keySet());

        for(String uniqueType : propertyTypes) {
            int typeCount = 0;
            for(String propertiesType : properties.keys()) {

                if (propertiesType.equals(uniqueType)) {
                    typeCount++;
                }
            }
            propertiesCount.put(typeCount, uniqueType);
        }

        return propertiesCount;
    }

    @Override
    public List<String> getUserPropertyByType(MultiValuedMap<String, String> allPropertyAddresses, String type) {

        List<String> addresses = new ArrayList<>();

        for(Map.Entry<String, String> entry : allPropertyAddresses.entries()) {
            if (entry.getKey().equals(type)) {
                addresses.add(entry.getValue());
            }
        }

        return addresses;
    }

}
