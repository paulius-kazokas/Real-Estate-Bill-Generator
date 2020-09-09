package repositories;

import config.DatabaseConfig;
import entities.Indicator;
import entities.User;
import interfaces.IIndicatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.SystemConstants.*;

public class IndicatorRepository implements IIndicatorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorRepository.class);

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Indicator> getIndicators(int propertyId) {

        String query = String.format("SELECT %s FROM %s WHERE %s = %s",
                SELECT_ALL,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_PROPERTY_ID, propertyId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<Indicator> indicators = new ArrayList<>();

            while (resultSet.next()) {

                indicators.add(Indicator.builder()
                        .id(resultSet.getInt(UTC_INDICATORS_TABLE_ID))
                        .propertyId(resultSet.getInt(UTC_INDICATORS_TABLE_PROPERTY_ID))
                        .utilityId(resultSet.getInt(UTC_INDICATORS_TABLE_UTILITY_ID))
                        .date(resultSet.getString(UTC_INDICATORS_TABLE_DATE))
                        .monthStartAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT))
                        .monthEndAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_END_AMOUNT))
                        .build()
                );
            }

            return indicators;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<Indicator> getIndicatorsByPropertyId(int propertyId, User user) {

        String queryIndicatorsId = String.format("SELECT %s FROM %s WHERE %s = %s", // cia enhancinti query
                UTC_INDICATORS_TABLE_ID,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_PROPERTY_ID, propertyId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {

            List<Indicator> indicators = new ArrayList<>();

            while (resultSet.next()) {
                indicators.addAll(getIndicators(resultSet.getInt(UTC_INDICATORS_TABLE_ID)));
            }
            return indicators;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
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
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public Integer getUtilityIdByIndicatorId(Integer indicatorId) {

        String query = String.format("SELECT %s FROM %s WHERE %s = %s",
                UTC_INDICATORS_TABLE_UTILITY_ID,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, indicatorId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("utility_id");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return null;
    }

}
