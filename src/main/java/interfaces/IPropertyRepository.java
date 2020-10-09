package interfaces;

import entities.Property;
import entities.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPropertyRepository {

    Set<Property> getPropertiesByUser(User user);

    Map<Integer, String> getUserProperties(User user);

    Property getPropertyByAddress(String address);

    List<Property> getPropertiesByType(User user, String type);

}
