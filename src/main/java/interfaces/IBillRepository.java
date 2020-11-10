package interfaces;

import entities.Property;
import org.json.JSONObject;

import java.sql.SQLException;

public interface IBillRepository {

    void saveBill(Property property, JSONObject bill) throws SQLException;
}
