package interfaces;

import java.util.List;

public interface IndicatorInterface {

    List<Integer> getIndicatorIdsByPropertyId(Integer propertyId);

    List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId);

    Integer getUtilityIdByIndicatorId(Integer propertyId);
}
