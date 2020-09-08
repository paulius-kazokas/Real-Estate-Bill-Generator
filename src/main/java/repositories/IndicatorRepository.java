package repositories;

import config.DatabaseConfig;
import config.SystemConstants;
import entities.lIndicator;
import interfaces.IndicatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.SystemConstants.*;

public class IndicatorRepository implements IndicatorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorRepository.class);

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    //    @Override
//    public List<Integer> getIndicatorIdsByPropertyId(int propertyId) {
//
//        List<Integer> indicators = new ArrayList<>();
//        String queryIndicatorsId = String.format("SELECT id FROM %s WHERE property_id = %s", SystemConstants.UTC_INDICATORS_TABLE, propertyId);
//
//        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
//             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
//            while (resultSet.next()) {
//                indicators.add(resultSet.getInt(SystemConstants.UTC_INDICATORS_TABLE_ID));
//            }
//            return indicators;
//        } catch (SQLException e) {
//            LOGGER.error(String.format("%s", e));
//        }
//
//        return Collections.emptyList();
//    }

    @Override
    public lIndicator getIndicator(int propertyId) {

        String query = String.format("SELECT * FROM %s WHERE property_id = %s", SystemConstants.UTC_INDICATORS_TABLE, propertyId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                System.out.println("month start: " + resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT));
                return lIndicator.builder()
                        .id(resultSet.getInt(UTC_INDICATORS_TABLE_ID))
                        .propertyId(resultSet.getInt(UTC_INDICATORS_TABLE_PROPERTY_ID))
                        .utilityId(resultSet.getInt(UTC_INDICATORS_TABLE_UTILITY_ID))
                        .date(resultSet.getString(UTC_INDICATORS_TABLE_DATE))
                        .monthEndAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_START_AMOUNT))
                        .monthEndAmount(resultSet.getInt(UTC_INDICATORS_TABLE_MONTH_END_AMOUNT))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

    @Override
    public List<lIndicator> getIndicatorsByPropertyId(int propertyId) {

        List<lIndicator> indicators = new ArrayList<>();
        String queryIndicatorsId = String.format("SELECT id FROM %s WHERE property_id = %s", SystemConstants.UTC_INDICATORS_TABLE, propertyId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
            while (resultSet.next()) {
                indicators.add(getIndicator(resultSet.getInt(UTC_INDICATORS_TABLE_ID)));
            }
            return indicators;
        } catch (SQLException e) {
            LOGGER.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getIndicatorMonthStartEndAmountsByIndicatorId(int indicatorId) {

        List<String> monthStartEndAmounts = new ArrayList<>();

        String queryIndicatorsId = "SELECT month_start_amount, month_end_amount FROM utc.indicator WHERE id = " + indicatorId;

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
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

        String queryUtilityId = "SELECT utility_id FROM utc.indicator WHERE id = " + indicatorId;

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryUtilityId)) {
            if (resultSet.next()) {
                return resultSet.getInt("utility_id");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return null;
    }
}
