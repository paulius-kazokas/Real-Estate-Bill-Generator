package interfaces;

import entities.Indicator;
import entities.User;

import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicators(int propertyId);

    List<Indicator> getIndicatorsByPropertyId(int propertyId, User user);

    List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId);

    Integer getUtilityIdByIndicatorId(Integer propertyId);

}
