package interfaces;

import entities.Utility;

import java.util.List;

public interface IUtilityRepository {

    Utility getUtility(int id);

    List<Utility> getUtilitiesByAddress(String address);

    List<Utility> getUtilityListByIndicatorId(Integer indicatorId);
}
