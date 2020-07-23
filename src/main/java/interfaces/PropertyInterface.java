package interfaces;

import entities.Property;
import entities.User;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;
import java.util.Map;

public interface PropertyInterface {

    //MultiValuedMap<String, String> getUserProperties(String userPersonalCode);
    List<Property> getUserProperties(User user);

    boolean userHasProperties(List<Property> properties);

    Map<Integer, String> getUserPropertiesCount(MultiValuedMap<String, String> properties);

    Integer getPropertyIdByPropertyAddress(String address);

    List<String> getProprtyAddressByPropertyType(String propertyType);
}
