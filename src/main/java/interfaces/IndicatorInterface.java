package interfaces;

import repositories.PropertyRepository;

import java.util.Map;

public interface IndicatorInterface {

    Integer getIndicatorIdByPropertyId(Integer propertyId);

    Map<Integer, String> getIndicatorsMonthStartEndAmountByIndicatorId(Integer propertyId);

    Map<Integer, String> getPropertyIndicatorsByPropertyAddress(PropertyRepository pr, String address);

}
