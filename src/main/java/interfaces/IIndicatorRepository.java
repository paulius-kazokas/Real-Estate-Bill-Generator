package interfaces;

import entities.Indicator;
import entities.Property;
import entities.User;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicatorsByProperty(String type, String address) throws SQLException;

    List<String> getIndicatorsByAddressAndUtilityName(User user, Property property, String utilityName) throws SQLException;

    Indicator getIndicator(Property property, Integer utilityId, String date) throws SQLException;

}
