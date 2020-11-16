package interfaces;

import entities.Bill;
import entities.User;
import org.json.JSONObject;

import java.sql.SQLException;

public interface IBillRepository {

    void saveBill(User user, String filteringCmd, JSONObject bill) throws SQLException;

    Bill getBill(String filter) throws SQLException;
}
