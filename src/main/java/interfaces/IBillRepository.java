package interfaces;

import entities.Bill;
import entities.User;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public interface IBillRepository {

    void saveBill(User user, String filteringCmd, JSONObject bill) throws SQLException;

    Bill getBill(User user, String filter) throws SQLException;

    List<Bill> getBills(User user) throws SQLException;

}
