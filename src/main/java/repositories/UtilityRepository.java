package repositories;

import config.DatabaseConfig;
import interfaces.UtilityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilityRepository implements UtilityInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilityRepository.class);

    DatabaseConfig databaseConfig;

    public UtilityRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public String getUtilityNameByUtilityId(Integer utilityId) {

        String query = "SELECT name FROM utc.utility WHERE id = " + utilityId;

        try (Statement statement = databaseConfig.connectionToDatabase().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if(resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }
}
