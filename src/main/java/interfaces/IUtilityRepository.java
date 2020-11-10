package interfaces;

import entities.Utility;

import java.sql.SQLException;

public interface IUtilityRepository {

    Utility getUtility(Integer id) throws SQLException;

    Utility getUtility(String utilityName) throws SQLException;

}
