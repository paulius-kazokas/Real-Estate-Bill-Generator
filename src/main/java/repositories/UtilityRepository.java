package repositories;

import config.DatabaseConfig;
import entities.Utility;
import interfaces.UtilityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static config.SystemConstants.*;

public class UtilityRepository implements UtilityInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilityRepository.class);

    DatabaseConfig databaseConfig;

    public UtilityRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Utility getUtility(Integer utilityId) {

        String query = String.format("SELECT %s FROM %s WHERE %s = %s",
                SELECT_ALL,
                UTC_UTILITY_TABLE,
                UTC_UTILITY_TABLE_ID, utilityId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if(resultSet.next()) {
                return Utility.builder()
                        .utilityId(resultSet.getInt(UTC_UTILITY_TABLE_ID))
                        .utilityName(resultSet.getString(UTC_UTILITY_TABLE_NAME))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

}
