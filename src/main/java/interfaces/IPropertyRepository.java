package interfaces;

import entities.Property;
import entities.User;

import java.sql.SQLException;
import java.util.Set;

public interface IPropertyRepository {

    Property getPropertyByIndicatorId(Integer id) throws SQLException;

    Set<Property> getPropertiesByUser(User user) throws SQLException;

    Property getPropertyByAddress(String address) throws SQLException;

}
