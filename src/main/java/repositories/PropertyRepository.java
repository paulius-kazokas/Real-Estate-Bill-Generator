package repositories;

import config.DatabaseConfig;
import entities.Property;
import entities.User;
import interfaces.IPropertyRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PropertyRepository implements IPropertyRepository {

    private DatabaseConfig databaseConfig;
    private UserRepository userRepository;

    public PropertyRepository(UserRepository userRepository, DatabaseConfig databaseConfig) {
        this.userRepository = userRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Property getPropertyByIndicatorId(Integer id) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(
                String.format("SELECT * FROM utc.property WHERE id IN (SELECT property_id FROM utc.indicator WHERE property_id = %s)", id));

        if (resultSet.next()) {
            Property property = Property.object();
            property.setId(id);
            property.setUser(userRepository.getUserByPropertyId(id));
            property.setPropertyType(resultSet.getString("property_type"));
            property.setAddress(resultSet.getString("address"));

            return property;
        }

        return null;
    }

    @Override
    public Set<Property> getPropertiesByUser(User user) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.property WHERE personal_code = %s", user.getPersonalCode()));

        Set<Property> properties = new HashSet<>();
        while (resultSet.next()) {
            Property property = Property.object();
            property.setId(resultSet.getInt("id"));
            property.setUser(user);
            property.setPropertyType(resultSet.getString("property_type"));
            property.setAddress(resultSet.getString("address"));

            properties.add(property);
        }

        return properties;
    }

    @Override
    public Property getPropertyByAddress(String address) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.property WHERE address = '%s'", address));
        Property property = Property.object();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");

            property.setId(id);
            property.setUser(userRepository.getUserByPropertyId(id));
            property.setPropertyType(resultSet.getString("property_type"));
            property.setAddress(resultSet.getString("address"));
        }

        return property;
    }

}
