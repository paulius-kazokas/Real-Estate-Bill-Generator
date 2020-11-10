package repositories;

import config.DatabaseConfig;
import entities.Indicator;
import interfaces.IIndicatorRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static config.SystemConstants.FORMATTER;

@Slf4j
public class IndicatorRepository implements IIndicatorRepository {

    DatabaseConfig databaseConfig;
    PropertyRepository propertyRepository;
    UtilityRepository utilityRepository;

    public IndicatorRepository(PropertyRepository propertyRepository, UtilityRepository utilityRepository, DatabaseConfig databaseConfig) {
        this.propertyRepository = propertyRepository;
        this.utilityRepository = utilityRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Indicator> getIndicatorsByProperty(String type, String address) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.indicator WHERE property_id IN (SELECT id FROM utc.property WHERE property_type = '%s' AND address = '%s')", type, address));

        List<Indicator> indicators = new ArrayList<>();
        while (resultSet.next()) {
            Indicator indicator = Indicator.object();
            indicator.setId(resultSet.getInt("id"));
            indicator.setProperty(propertyRepository.getPropertyByIndicatorId(resultSet.getInt("property_id")));
            indicator.setUtility(utilityRepository.getUtility(resultSet.getInt("utility_id")));
            indicator.setDate(FORMATTER.parseDateTime(resultSet.getString("date")));
            indicator.setMonthStartAmount(resultSet.getInt("month_start_amount"));
            indicator.setMonthEndAmount(resultSet.getInt("month_end_amount"));

            indicators.add(indicator);
        }
        resultSet.close();
        return indicators;

    }

}
