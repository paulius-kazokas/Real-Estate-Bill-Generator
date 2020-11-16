package repositories;

import config.DatabaseConfig;
import entities.Bill;
import entities.User;
import interfaces.IBillRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public void saveBill(User user, String filteringCmd, JSONObject bill) throws SQLException {

        String query = "INSERT INTO utc.bill (personal_code, filtering_cmd, bill_json) VALUES (?, ?, ?)";
        Connection connection = databaseConfig.connectionToDatabase();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getPersonalCode());
            preparedStatement.setString(2, filteringCmd);
            preparedStatement.setString(3, bill.toString());

            preparedStatement.execute();
        }
        connection.close();

        OUT.write("Saved bill...".getBytes());
    }

    @Override
    public Bill getBill(String filter) throws SQLException {

        ResultSet resultSet = databaseConfig.resultSet(String.format("SELECT * FROM utc.bill WHERE filtering_cmd = '%s'", filter));

        if (resultSet.next() && resultSet.getString("filtering_cmd").equals(filter)) {
            Bill bill = Bill.object();
            bill.setId(resultSet.getInt("id"));
            bill.setUser(userRepository.getUserByPersonalCode(resultSet.getString("personal_code")));
            bill.setFilteringCmd(filter);
            bill.setBillJson(resultSet.getString("bill_json"));

            return bill;
        }
        resultSet.close();

        return null;
    }

}
