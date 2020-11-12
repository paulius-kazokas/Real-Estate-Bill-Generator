package interfaces;

import entities.Indicator;
import entities.Utility;

import java.sql.SQLException;
import java.util.List;

public interface IUtilityRepository {

    Utility getUtility(Integer id) throws SQLException;

    Utility getUtility(String utilityName) throws SQLException;

    List<Utility> getUtilities(Indicator indicator) throws SQLException;

}
