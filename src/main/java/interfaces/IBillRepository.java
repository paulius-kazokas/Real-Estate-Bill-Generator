package interfaces;

import entities.User;
import org.json.JSONObject;

import java.sql.SQLException;

public interface IBillRepository {

    void saveBill(User user, String filteringCmd, JSONObject bill) throws SQLException;
}
