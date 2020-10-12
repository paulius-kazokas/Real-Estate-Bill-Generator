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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public Map<Integer, Property> propertiesTotMap(List<Property> elementList) {

        Map<Integer, Property> elementMap = IntStream.range(0, elementList.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        elementList::get,
                        (k, v) -> k, LinkedHashMap::new
                ));

        elementMap.forEach((k, v) -> System.out.println(String.format("%s. %s", k, v)));

        return elementMap;
    }

    @Override
    public Set<Property> getPropertiesByUser(User user) {

        String personalCode = user.getPersonalCode();

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT %s, %s, %s FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_ID, UTC_PROPERTY_TABLE_TYPE, UTC_PROPERTY_TABLE_ADDRESS,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, personalCode), personalCode);

        try {
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

        String personalCode = user.getPersonalCode();

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT DISTINCT(%s) FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_TYPE,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, personalCode), personalCode);

        try {
            List<String> propertyTypes = new ArrayList<>();
            while (resultSet.next()) {
                propertyTypes.add(resultSet.getString(UTC_PROPERTY_TABLE_TYPE));
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

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s'",
                SELECT_ALL,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_ADDRESS, address), address);

        try {
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

        String personalCode = user.getPersonalCode();

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT %s FROM %s WHERE %s = '%s' AND %s = '%s'",
                SELECT_ALL,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_TYPE, type,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, personalCode), type, personalCode);

        try {
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
