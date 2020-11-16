package interfaces;

import entities.Indicator;
import entities.Property;
import entities.Utility;

import java.sql.SQLException;
import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicatorsByProperty(String type, String address) throws SQLException;

    List<String> getIndicatorDatesByPropertyAndUtility(Property property, Utility utility) throws SQLException;

    Indicator getIndicatorsByPropertyUtilityAndDate(Property property, Utility utility, String date) throws SQLException;

    List<Indicator> getIndicators(Property property, String date) throws SQLException;

}
