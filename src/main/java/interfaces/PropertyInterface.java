package interfaces;

import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;
import java.util.Map;

public interface PropertyInterface {

    MultiValuedMap<String, String> getUserProperties(String userPersonalCode);

    boolean userHasProperties(MultiValuedMap<String, String> properties);

    Map<Integer, String> getUserPropertiesCount(MultiValuedMap<String, String> properties);

    List<String> getUserPropertyByType(MultiValuedMap<String, String> allPropertyAddresses, String type);
}
