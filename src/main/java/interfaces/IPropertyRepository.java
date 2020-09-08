package interfaces;

import entities.lProperty;
import entities.lUser;
import repositories.UserRepository;

import java.util.List;
import java.util.Map;

public interface IPropertyRepository {

    List<lProperty> getPropertiesByUser(lUser user);

    Map<Integer, String> getUserPropertiesCount(lUser user);

    lProperty getPropertyByAddress(String address);

    List<lProperty> getPropertiesByType(lUser user, String type);

}
