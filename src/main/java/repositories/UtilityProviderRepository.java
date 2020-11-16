package repositories;

import config.DatabaseConfig;
import entities.UtilityProvider;
import interfaces.IUtilityProviderRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UtilityProviderRepository implements IUtilityProviderRepository {

    DatabaseConfig databaseConfig;

    public UtilityProviderRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public UtilityProvider getUtilityProvider(Integer utilityId) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.utility_provider WHERE id = %s", utilityId));
        UtilityProvider utilityProvider = UtilityProvider.object();

        if (resultSet.next()) {
            utilityProvider.setId(resultSet.getInt("id"));
            utilityProvider.setName(resultSet.getString("name"));
            utilityProvider.setAdditionalInformation(resultSet.getString("additional_info"));
        }
        resultSet.close();

        return utilityProvider;
    }
}
