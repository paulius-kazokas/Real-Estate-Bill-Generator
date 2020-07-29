package interfaces;

import entities.Property;
import entities.User;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;
import java.util.Map;

public interface IPropertyRepository {

    List<Property> getPropertiesByUser(User user);

    Map<Integer, String> getUserPropertiesCount(User user);

    Property getPropertyByAddress(String address);

    List<Property> getPropertiesByType(User user, String type);

}
