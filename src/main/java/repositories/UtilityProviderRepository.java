package repositories;

import config.DatabaseConfig;
import entities.Utility;
import entities.UtilityProvider;
import interfaces.IUtilityProvider;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static config.SystemConstants.*;

@Slf4j
public class UtilityProviderRepository implements IUtilityProvider {

    DatabaseConfig databaseConfig;

    public UtilityProviderRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public UtilityProvider getUtilityProvider(int utilityId) {

        String query = String.format("SELECT %s FROM %S WHERE %S = %s",
                SELECT_ALL,
                UTC_UTILITY_PROVIDER_TABLE,
                UTC_UTILITY_PROVIDER_TABLE_ID, utilityId);

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                UtilityProvider utilityProvider = UtilityProvider.object();
                utilityProvider.setId(resultSet.getInt(UTC_UTILITY_PROVIDER_TABLE_ID));
                utilityProvider.setName(resultSet.getString(UTC_UTILITY_PROVIDER_TABLE_NAME));
                utilityProvider.setAdditionalInformation(resultSet.getString(UTC_UTILITY_PROVIDER_TABLE_ADDITIONAL_INFO));

                return utilityProvider;
            }

        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return null;
    }
}
