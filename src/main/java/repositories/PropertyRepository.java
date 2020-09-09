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

import static config.SystemConstants.*;

public class PropertyRepository implements IPropertyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    private DatabaseConfig databaseConfig;
    private UserRepository userRepository;

    public PropertyRepository(UserRepository userRepository, DatabaseConfig databaseConfig) {
        this.userRepository = userRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Property> getPropertiesByUser(User user) {

        String query = String.format("SELECT %s, %s, %s FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_ID, UTC_PROPERTY_TABLE_TYPE, UTC_PROPERTY_TABLE_ADDRESS,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode());

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Property> properties = new ArrayList<>();

            while (resultSet.next()) {
                Property property = Property.builder()
                        .id(resultSet.getInt(UTC_PROPERTY_TABLE_ID))
                        .ownerPersonalCode(user.getPersonalCode())
                        .type(resultSet.getString(UTC_PROPERTY_TABLE_TYPE))
                        .address(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS))
                        .build();

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
            LOGGER.error(String.format("%s", e));
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
                return Property.builder()
                        .user(userRepository.getUser(resultSet.getInt(UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE)))
                        .id(resultSet.getInt(UTC_PROPERTY_TABLE_ID))
                        .ownerPersonalCode(resultSet.getString(UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE))
                        .type(resultSet.getString(UTC_PROPERTY_TABLE_TYPE))
                        .address(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
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

                Property returnProperty = Property.builder()
                        .id(resultSet.getInt(UTC_PROPERTY_TABLE_ID))
                        .ownerPersonalCode(resultSet.getString(UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE))
                        .type(resultSet.getString(UTC_PROPERTY_TABLE_TYPE))
                        .address(resultSet.getString(UTC_PROPERTY_TABLE_ADDRESS))
                        .build();

                properties.add(returnProperty);
            }

            return properties;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

}
