package repositories;

import config.DatabaseConfig;
import interfaces.IndicatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class IndicatorRepository implements IndicatorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorRepository.class);

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Integer getIndicatorIdByPropertyId(Integer propertyId) {

        String queryIndicatorsId = "SELECT id FROM utc.indicator WHERE property_id = " + propertyId;

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public Map<Integer, String> getIndicatorsMonthStartEndAmountByIndicatorId(Integer indicatorId) {

        String queryIndicators = "SELECT month_start_amount, month_end_amount FROM utc.indicator WHERE id =" + indicatorId;

        Map<Integer, String> indicators = new HashMap<>();
        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(queryIndicators)) {
            if (resultSet.next()) {
                indicators.put(indicatorId, resultSet.getString("month_start_amount") + "," + resultSet.getString("month_end_amount"));
            }
            return indicators;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public Map<Integer, String> getPropertyIndicatorsByPropertyAddress(PropertyRepository pr, String address) {

        Integer propertyId = pr.getPropertyIdByPropertyAddress(address);
        Integer indicatorId = getIndicatorIdByPropertyId(propertyId);

        return getIndicatorsMonthStartEndAmountByIndicatorId(indicatorId);
    }

}
