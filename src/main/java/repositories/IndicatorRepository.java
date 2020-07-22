package repositories;

import config.DatabaseConfig;
import interfaces.IndicatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndicatorRepository implements IndicatorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorRepository.class);

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Integer> getIndicatorIdsByPropertyId(Integer propertyId) {

        List<Integer> indicators = new ArrayList<>();

        String queryIndicatorsId = "SELECT id FROM utc.indicator WHERE property_id = " + propertyId;

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
            while (resultSet.next()) {
                indicators.add(resultSet.getInt("id"));
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
                monthStartEndAmounts.add(resultSet.getString("month_start_amount") + "," + resultSet.getString("month_end_amount"));
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
