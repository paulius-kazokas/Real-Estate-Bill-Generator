package repositories;

import config.DatabaseConfig;
import entities.Utility;
import interfaces.IUtilityRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UtilityRepository implements IUtilityRepository {

    private DatabaseConfig databaseConfig;
    private UtilityProviderRepository utilityProviderRepository;

    public UtilityRepository(UtilityProviderRepository utilityProviderRepository, DatabaseConfig databaseConfig) {
        this.utilityProviderRepository = utilityProviderRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Utility getUtility(Integer utiityId) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.utility WHERE id = %s", utiityId));

        if (resultSet.next()) {
            Utility utility = Utility.object();
            utility.setId(resultSet.getInt("id"));
            utility.setUtilityProvider(utilityProviderRepository.getUtilityProvider(resultSet.getInt("utility_provider_id")));
            utility.setName(resultSet.getString("name"));
            utility.setComment(resultSet.getString("comment"));

            return utility;
        }

        return null;
    }

}
