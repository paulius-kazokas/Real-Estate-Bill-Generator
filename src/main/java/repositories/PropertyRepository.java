package repositories;

import config.DatabaseConfig;
import config.SystemConstants;
import entities.lProperty;
import entities.lUser;
import interfaces.IPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PropertyRepository implements IPropertyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    private DatabaseConfig databaseConfig;
    private UserRepository userRepository;

    public PropertyRepository(UserRepository userRepository, DatabaseConfig databaseConfig) {
        this.userRepository = userRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<lProperty> getPropertiesByUser(lUser user) {

        List<lProperty> properties = new ArrayList<>();

        String query = "SELECT id, type, address FROM utc.property WHERE ownderPersonalCode = '" + user.getPersonalCode() + "'";
        System.out.println(query);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                lProperty property = lProperty.builder()
                        .id(resultSet.getInt("id"))
                        .ownerPersonalCode(user.getPersonalCode())
                        .type(resultSet.getString("type"))
                        .address(resultSet.getString("address"))
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
    public Map<Integer, String> getUserPropertiesCount(lUser user) {

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
    public lProperty getPropertyByAddress(String address) {

        String query = "SELECT * FROM " + SystemConstants.UTC_PROPERTY_TABLE + " WHERE address ='" + address + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                lProperty property = lProperty.builder()
                        .user(userRepository.getUser(resultSet.getInt("ownderPersonalCode")))
                        .id(resultSet.getInt("id"))
                        .ownerPersonalCode(resultSet.getString("ownderPersonalCode"))
                        .type(resultSet.getString("type"))
                        .address(resultSet.getString("address"))
                        .build();

                System.out.println(property);
                return property;
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public List<lProperty> getPropertiesByType(lUser user, String type) {

        String query = "SELECT * FROM utc.property WHERE type = '" + type + "' AND ownderPersonalCode = '" + user.getPersonalCode() + "'";

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<lProperty> properties = new ArrayList<>();
            while (resultSet.next()) {

                lProperty returnProperty = lProperty.builder()
                        .id(resultSet.getInt("id"))
                        .ownerPersonalCode(resultSet.getString("ownderPersonalCode"))
                        .type(resultSet.getString("type"))
                        .address(resultSet.getString("address"))
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
