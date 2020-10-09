package repositories;

import config.DatabaseConfig;
import entities.Property;
import entities.User;
import interfaces.IPropertyRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static config.SystemConstants.*;

@Slf4j
public class PropertyRepository implements IPropertyRepository {

    private DatabaseConfig databaseConfig;
    private UserRepository userRepository;
    private IndicatorRepository indicatorRepository;

    public PropertyRepository(UserRepository userRepository, IndicatorRepository indicatorRepository, DatabaseConfig databaseConfig) {
        this.userRepository = userRepository;
        this.indicatorRepository = indicatorRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Set<Property> getPropertiesByUser(User user) {

        String query = String.format("SELECT %s, %s, %s FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_ID, UTC_PROPERTY_TABLE_TYPE, UTC_PROPERTY_TABLE_ADDRESS,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode());

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            Set<Property> properties = new HashSet<>();

            while (resultSet.next()) {
                Property property = Property.object();
                property.setId(resultSet.getInt(UTC_PROPERTY_TABLE_ID));
                property.setIndicators(indicatorRepository.getIndicatorsByUser(user));
                property.setUser(user);
                property.setType(resultSet.getString(UTC_PROPERTY_TABLE_TYPE));
                property.setAddress(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS));

                properties.add(property);
            }

            return properties;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptySet();
    }

    @Override
    public Map<Integer, String> getUserProperties(User user) {

        String typeQuery = String.format("SELECT DISTINCT(%s) FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_TYPE,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode());

        try (Statement typeStatement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet typeResultSet = typeStatement.executeQuery(typeQuery)) {

            List<String> propertyTypes = new ArrayList<>();

            while (typeResultSet.next()) {
                propertyTypes.add(typeResultSet.getString(UTC_PROPERTY_TABLE_TYPE));
            }

            Map<Integer, String> propertiesCount = new HashMap<>();
            for (String type : propertyTypes) {

                String propertyQuery = String.format("SELECT count(%s) AS COUNT FROM %s WHERE %s = '%s' AND %s = '%s'",
                        UTC_PROPERTY_TABLE_TYPE,
                        UTC_PROPERTY_TABLE,
                        UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode(),
                        UTC_PROPERTY_TABLE_TYPE, type);

                try (Statement countStatement = databaseConfig.connectionToDatabase().createStatement();
                     ResultSet countResultSet = countStatement.executeQuery(propertyQuery)) {

                    while (countResultSet.next()) {
                        propertiesCount.put(countResultSet.getInt("count"), type);
                    }
                }
            }

            return propertiesCount;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public Property getPropertyByAddress(String address) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                SELECT_ALL,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_ADDRESS, address);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int id = resultSet.getInt(UTC_PROPERTY_TABLE_ID);

                Property property = Property.object();
                property.setUser(userRepository.getUser(resultSet.getString(UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE)));
                property.setId(id);
                property.setIndicators(indicatorRepository.getIndicatorsByPropertyId(id));
                property.setType(resultSet.getString(UTC_PROPERTY_TABLE_TYPE));
                property.setAddress(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS));

                return property;
            }
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public List<Property> getPropertiesByType(User user, String type) {

        String query = String.format("SELECT %s FROM %s WHERE %s = '%s' AND %s = '%s'",
                SELECT_ALL,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_TYPE, type,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode());

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Property> properties = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt(UTC_PROPERTY_TABLE_ID);

                Property property = Property.object();
                property.setId(id);
                property.setIndicators(indicatorRepository.getIndicatorsByPropertyId(id));
                property.setUser(user);
                property.setType(resultSet.getString(UTC_PROPERTY_TABLE_TYPE));
                property.setAddress(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS));

                properties.add(property);
            }

            return properties;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

}
