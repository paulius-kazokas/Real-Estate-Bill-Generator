package interfaces;

import entities.UtilityProvider;

import java.sql.SQLException;

public interface IUtilityProviderRepository {

    UtilityProvider getUtilityProvider(Integer utilityId) throws SQLException;
}
