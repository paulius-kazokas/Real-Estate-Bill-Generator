package repositories;

import config.DatabaseConfig;
import entities.Utility;
import interfaces.IUtilityRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.SystemConstants.*;

@Slf4j
public class UtilityRepository implements IUtilityRepository {

    DatabaseConfig databaseConfig;
    UtilityProviderRepository utilityProviderRepository;

    public UtilityRepository(UtilityProviderRepository utilityProviderRepository, DatabaseConfig databaseConfig) {
        this.utilityProviderRepository = utilityProviderRepository;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Utility getUtility(Integer indicatorId) {

        ResultSet resultSet = databaseConfig.resultSet(String.format(
                "SELECT %s FROM %s " +
                        "WHERE %s = (SELECT %s FROM %s " +
                        "WHERE %s = %s)",
                SELECT_ALL, UTC_UTILITY_TABLE,
                UTC_UTILITY_TABLE_ID, UTC_INDICATORS_TABLE_UTILITY_ID, UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, indicatorId), indicatorId.toString());

        try {
            if (resultSet.next()) {
                Utility utility = Utility.object();
                int id = resultSet.getInt(UTC_UTILITY_TABLE_ID);

                utility.setId(id);
                utility.setUtilityProvider(utilityProviderRepository.getUtilityProvider(id));
                utility.setName(resultSet.getString(UTC_UTILITY_TABLE_NAME));
                utility.setComment(resultSet.getString(UTC_UTILITY_TABLE_COMMENT));

                return utility;
            }
        } catch (SQLException e) {
            log.error(e.toString());
        }

        return null;
    }

    @Override
    public List<Utility> getUtilitiesByAddress(String address) {

//        SELECT * FROM indicator
//        WHERE id IN (SELECT id FROM indicator
//                WHERE id IN (SELECT indicator_id FROM property
//                        WHERE address = 'Akropolio g. 10, LT-12345, Vilnius'));
        ResultSet resultSet = databaseConfig.resultSet(String.format(
                "SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = '%s'))",
                SELECT_ALL, UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, UTC_INDICATORS_TABLE_ID, UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, UTC_PROPERTY_TABLE_INDICATOR_ID, UTC_PROPERTY_TABLE,
                UTC_PROPERTY_TABLE_ADDRESS, address), address);


        try {
            List<Utility> utilities = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(UTC_UTILITY_TABLE_ID);

                Utility utility = Utility.object();
                utility.setId(id);
                utility.setUtilityProvider(utilityProviderRepository.getUtilityProvider(id));
                utility.setName(resultSet.getString(UTC_UTILITY_TABLE_NAME));
                utility.setComment(resultSet.getString(UTC_UTILITY_TABLE_COMMENT));

                utilities.add(utility);
            }

            return utilities;
        } catch (SQLException e) {
            log.error(String.format("%s", e));
        }

        return Collections.emptyList();
    }

    @Override
    public List<Utility> getUtilityListByIndicatorId(Integer indicatorId) {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT %s FROM %s WHERE %s = %s",
                SELECT_ALL,
                UTC_INDICATORS_TABLE,
                UTC_INDICATORS_TABLE_ID, indicatorId), indicatorId.toString());

        try {
            List<Utility> utilities = new ArrayList<>();
            while (resultSet.next()) {
                Utility utility = Utility.object();
                int id = resultSet.getInt(UTC_UTILITY_TABLE_ID);

                utility.setId(id);
                utility.setUtilityProvider(utilityProviderRepository.getUtilityProvider(id));
                utility.setName(resultSet.getString(UTC_UTILITY_TABLE_NAME));
                utility.setComment(resultSet.getString(UTC_UTILITY_TABLE_COMMENT));

                utilities.add(utility);
            }

            return utilities;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return Collections.emptyList();
    }

}
