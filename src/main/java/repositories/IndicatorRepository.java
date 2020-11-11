package repositories;

import config.DatabaseConfig;
import entities.Indicator;
import entities.Property;
import entities.Utility;
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

    @Override
    public List<String> getIndicatorDatesByPropertyAndUtility(Property property, Utility utility) throws SQLException {

        List<String> dates = new ArrayList<>();

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT DISTINCT date from utc.indicator WHERE property_id IN (SELECT id FROM utc.property WHERE address = '%s') AND utility_id IN (SELECT id FROM utc.utility WHERE name = '%s')", property.getAddress(), utility.getName()));

        while (resultSet.next()) {
            String date = resultSet.getString("date");
            dates.add(date);
        }

        resultSet.close();
        return dates;
    }


    @Override
    public Indicator getIndicatorsByPropertyUtiltyAndDate(Property property, Utility utility, String date) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT DISTINCT * from utc.indicator WHERE property_id IN (SELECT id FROM utc.property WHERE address = '%s') AND utility_id IN (SELECT id FROM utc.utility WHERE name = '%s') AND date = '%s';", property.getAddress(), utility.getName(), date));
        Indicator indicator = Indicator.object();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");

            indicator.setId(id);
            indicator.setProperty(propertyRepository.getPropertyByIndicatorId(id));
            indicator.setUtility(utilityRepository.getUtility(utility.getName()));
            indicator.setDate(FORMATTER.parseDateTime(resultSet.getString("date")));
            indicator.setMonthStartAmount(resultSet.getInt("month_start_amount"));
            indicator.setMonthEndAmount(resultSet.getInt("month_end_amount"));
        }

        resultSet.close();
        return indicator;
    }


}
