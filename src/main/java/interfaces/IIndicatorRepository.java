package interfaces;

import entities.Indicator;
import entities.User;

import java.util.List;

public interface IIndicatorRepository {

    List<Indicator> getIndicators(int propertyId);

    List<Indicator> getIndicatorsByPropertyId(int propertyId);

    List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId);

    List<Indicator> getIndicatorsByUser(User user);

    List<Indicator> getIndicatorsByProperty(String type, String address);

}
