package interfaces;

import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;
import java.util.Map;

public interface PropertyInterface {

    MultiValuedMap<String, String> getUserProperties(String userPersonalCode);

    boolean userHasProperties(MultiValuedMap<String, String> properties);

    Map<Integer, String> getUserPropertiesCount(MultiValuedMap<String, String> properties);

    Integer getPropertyIdByPropertyAddress(String address);

    List<String> getProprtyAddressByPropertyType(String propertyType);
}
