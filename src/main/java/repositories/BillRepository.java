package repositories;

import config.DatabaseConfig;
import entities.Property;
import interfaces.IBillRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static config.SystemConstants.OUT;

public class BillRepository implements IBillRepository {

    DatabaseConfig databaseConfig;
    UserRepository userRepository;

    public BillRepository(UserRepository userRepository, DatabaseConfig databaseConfig) {
        this.userRepository = userRepository;
        this.databaseConfig = databaseConfig;
    }

    @SneakyThrows(IOException.class)
    @Override
    public void saveBill(Property property, JSONObject bill) throws SQLException {

        String query = "INSERT INTO utc.bill (personal_code, filtering_cmd, bill_json) VALUES (?, ?, ?)";
        Connection connection = databaseConfig.connectionToDatabase();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, property.getUser().getPersonalCode());
            preparedStatement.setString(2, "empty_for_now");
            preparedStatement.setString(3, bill.toString());

            preparedStatement.execute();
        }
        connection.close();

        OUT.write("Saved bill...".getBytes());
    }

}
