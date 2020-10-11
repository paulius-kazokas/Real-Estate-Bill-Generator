package interfaces;

import entities.Indicator;
import entities.User;

import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicators(Integer propertyId);

    List<Indicator> getIndicatorsByPropertyId(Integer propertyId);

    List<String> getIndicatorMonthStartEndAmountsByIndicatorId(Integer indicatorId);

    List<Indicator> getIndicatorsByUser(User user);

    List<Indicator> getIndicatorsByProperty(String type, String address);

}
