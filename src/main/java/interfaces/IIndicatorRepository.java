package interfaces;

import entities.Indicator;

import java.sql.SQLException;
import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicatorsByProperty(String type, String address) throws SQLException;

}
