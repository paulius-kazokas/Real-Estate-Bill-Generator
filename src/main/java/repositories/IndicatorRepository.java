package repositories;

import config.DatabaseConfig;
import interfaces.IndicatorInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class IndicatorRepository implements IndicatorInterface {

    DatabaseConfig databaseConfig;

    public IndicatorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    // TODO: principle works, but REDO to more efficient way of getting indicators (hint: one query would be great)
    @Override
    public Map<String, String> getpropertyIndicatorsByPropertyAddress(String address) {

        String queryAddressId = "SELECT id FROM utc.property WHERE address ='" + address + "'";
        Integer addressId = null;
        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(queryAddressId)) {
            while (resultSet.next()) {
                addressId = resultSet.getInt("id");
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        String queryIndicatorsId = "SELECT id FROM utc.indicator WHERE property_id = " + addressId;
        Integer indicatorId = null;
        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(queryIndicatorsId)) {
            while (resultSet.next()) {
                indicatorId = resultSet.getInt("id");
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        String queryIndicators = "SELECT month_start_amount, month_end_amount FROM utc.indicator WHERE id =" + indicatorId;
        Map<String, String> indicators = new HashMap<>();
        try (Statement statement = databaseConfig.connectionToDatabase().createStatement(); ResultSet resultSet = statement.executeQuery(queryIndicators)) {
            while (resultSet.next()) {
                indicators.put(resultSet.getString("month_start_amount"), resultSet.getString("month_end_amount"));
            }
            databaseConfig.connectionToDatabase().close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return indicators;
    }
}
