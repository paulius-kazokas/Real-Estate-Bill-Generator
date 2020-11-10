package repositories;

import config.DatabaseConfig;
import entities.Indicator;
import entities.Property;
import entities.User;
import entities.Utility;
import interfaces.IIndicatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<String> getIndicatorsByAddressAndUtilityName(User user, Property property, String utilityName) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT DISTINCT date FROM utc.indicator WHERE property_id = (SELECT id FROM utc.property WHERE address = '%s' AND personal_code = '%s') AND utility_id IN (SELECT id FROM utc.utility WHERE name = '%s')", property.getAddress(), user.getPersonalCode(), utilityName));
        List<String> dates = new ArrayList<>();

        while (resultSet.next()) {
            dates.add(resultSet.getString("date"));
        }

        resultSet.close();
        return dates;
    }

    @Override
    public Indicator getIndicator(Property property, Integer utilityId, String date) throws SQLException {

        Utility utility = utilityRepository.getUtility(utilityId);
        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.indicator WHERE property_id = %s AND utility_id = %s AND date = '%s'", property.getId(), utility.getId(), date));
        Indicator indicator = Indicator.object();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");

            indicator.setId(id);
            indicator.setProperty(propertyRepository.getPropertyByIndicatorId(id));
            indicator.setUtility(utilityRepository.getUtility(utilityId));
            indicator.setDate(FORMATTER.parseDateTime(resultSet.getString("date")));
            indicator.setMonthStartAmount(resultSet.getInt("month_start_amount"));
            indicator.setMonthEndAmount(resultSet.getInt("month_end_amount"));
        }
        resultSet.close();
        return indicator;
    }


}
