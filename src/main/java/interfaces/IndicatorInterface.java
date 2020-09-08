package interfaces;

import entities.lIndicator;

import java.util.List;

public interface IndicatorInterface {

    lIndicator getIndicator(int propertyId);

    //List<Integer> getIndicatorIdsByPropertyId(int propertyId);
    List<lIndicator> getIndicatorsByPropertyId(int propertyId);

    List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId);

    Integer getUtilityIdByIndicatorId(Integer propertyId);
}
