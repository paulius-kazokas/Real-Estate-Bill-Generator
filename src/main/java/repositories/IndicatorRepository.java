package repositories;

import config.DatabaseConfig;
import entities.Indicator;
import entities.User;
import entities.Utility;
import interfaces.IIndicatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.SystemConstants.*;

@Slf4j
public class IndicatorRepository implements IIndicatorRepository {

    DatabaseConfig databaseConfig;
    UtilityRepository utilityRepository;

    public IndicatorRepository(UtilityRepository utilityRepository, DatabaseConfig databaseConfig) {
        this.utilityRepository = utilityRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Indicator> getIndicators(int indicatorId) {

        String query = String.format("SELECT %s FROM %s WHERE %s = %s",
                SELECT_ALL,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, indicatorId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Indicator> indicators = new ArrayList<>();

            while (resultSet.next()) {
                Indicator indicator = Indicator.object();

                indicator.setId(resultSet.getInt(UTC_INDICATORS_TABLE_ID));
                indicator.setUtility(utilityRepository.getUtility(indicatorId));
                indicator.setDate(FORMATTER.parseDateTime(resultSet.getString(UTC_INDICATORS_TABLE_DATE)));
                indicator.setMonthStartAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT));
                indicator.setMonthEndAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_END_AMOUNT));

                indicators.add(indicator);
            }

            return indicators;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<Indicator> getIndicatorsByPropertyId(int propertyId) {

        String query = String.format(
                "SELECT %s FROM %s " +
                    "WHERE %s IN (SELECT %s FROM %s " +
                    "WHERE %s = %s)",
                SELECT_ALL, UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, UTC_PROPERTY_TABLE_INDICATOR_ID, UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_ID, propertyId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Indicator> indicators = new ArrayList<>();

            while (resultSet.next()) {
                indicators.addAll(getIndicators(resultSet.getInt(UTC_INDICATORS_TABLE_ID)));
            }
            return indicators;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId) {

        String query = String.format("SELECT %s, %s FROM %s WHERE %s = %s",
                UTC_INDICATORS_TABLE_MONTH_START_AMOUNT, UTC_INDICATORS_TABLE_MONTH_END_AMOUNT,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, indicatorId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<String> monthStartEndAmounts = new ArrayList<>();

            while (resultSet.next()) {
                monthStartEndAmounts.add(resultSet.getString("month_start_amount") + "-" + resultSet.getString("month_end_amount"));
            }
            return monthStartEndAmounts;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<Indicator> getIndicatorsByUser(User user) {

        // get indicators ids by users personal code (property)
        String query1 = String.format("SELECT %s FROM %s WHERE %s = '%s'",
                UTC_PROPERTY_TABLE_INDICATOR_ID,
                UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_OWNER_PERSONAL_CODE, user.getPersonalCode());

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query1)) {

            List<Integer> indicatorsIds = new ArrayList<>();

            while (resultSet.next()) {
                indicatorsIds.add(resultSet.getInt(UTC_PROPERTY_TABLE_INDICATOR_ID));
            }

            // get indicators by ids from property table

            for (int indicatorId : indicatorsIds) {
                String query2 = String.format("SELECT %s FROM %s WHERE %s = %s",
                        SELECT_ALL,
                        UTC_INDICATORS_TABLE,
                        UTC_INDICATORS_TABLE_ID, indicatorId);

                try (Statement statement2 = databaseConfig.connectionToDatabase().createStatement();
                     ResultSet resultSet2 = statement2.executeQuery(query2)) {

                    List<Indicator> indicators = new ArrayList<>();

                    while (resultSet.next()) {
                        Indicator indicator = Indicator.object();
                        indicator.setId(indicatorId);
                        indicator.setUtility(utilityRepository.getUtility(indicatorId));
                        indicator.setDate(FORMATTER.parseDateTime(resultSet2.getString(UTC_INDICATORS_TABLE_DATE)));
                        indicator.setMonthStartAmount(resultSet2.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT));
                        indicator.setMonthEndAmount(resultSet2.getInt(UTC_INDICATORS_TABLE_MONTH_END_AMOUNT));

                        indicators.add(indicator);
                    }

                    return indicators;
                } catch (SQLException e) {
                    log.error(String.format("%s", e));
                }
            }

        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }
        return Collections.emptyList();
    }

    @Override
    public List<Indicator> getIndicatorsByProperty(String type, String address) {

        String query = String.format("SELECT %s FROM %s " +
                "WHERE %s IN (SELECT %s FROM %s\n" +
                "    WHERE %s = '%s' AND %s = '%s')",
                SELECT_ALL, UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, UTC_PROPERTY_TABLE_INDICATOR_ID, UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_TYPE, type, UTC_PROPERTY_TABLE_ADDRESS, address);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Indicator> indicators = new ArrayList<>();

            while(resultSet.next()) {
                int id = resultSet.getInt(UTC_INDICATORS_TABLE_ID);

                Indicator indicator = Indicator.object();
                indicator.setId(id);
                indicator.setUtility(utilityRepository.getUtility(id));
                indicator.setDate(FORMATTER.parseDateTime(resultSet.getString(UTC_INDICATORS_TABLE_DATE)));
                indicator.setMonthStartAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT));
                indicator.setMonthEndAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_END_AMOUNT));

                indicators.add(indicator);
            }

            return indicators;

        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }


}
