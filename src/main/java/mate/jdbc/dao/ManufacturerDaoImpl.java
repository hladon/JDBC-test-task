package mate.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mate.jdbc.lib.Dao;
import mate.jdbc.model.Manufacturer;
import mate.jdbc.util.ConnectionUtil;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {

    //TODO First you have to call executeUpdate() and only then getGeneratedKey().
    /*TODO It`s bad way to handle exceptions. It going to crush whole app and don`t give any information about a reason.
       You have to handle exception inside your catch or throw it further. Add more information about reason of exception.
       In this task was suggested to transform exception DataProcessingException you should do this.
     */
    //TODO Move query to separate variable.
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement createStatement =
                         connection.prepareStatement(
                             "INSERT INTO manufacturers(name, country) values(?,?);",
                             Statement.RETURN_GENERATED_KEYS)) {
            createStatement.setString(1, manufacturer.getName());
            createStatement.setString(2, manufacturer.getCountry());
            ResultSet generatedKeys = createStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                manufacturer.setId(id);
            }
            createStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
        return manufacturer;
    }
    //TODO Have to be returned Optional. If error occur or there is no record with such Id you can return Optional with Null.
    @Override
    public Manufacturer get(Long id) {
        Manufacturer manufacturer = new Manufacturer();
        String getManufacturerRequest = "SELECT * FROM manufacturers WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement getStatement =
                         connection.prepareStatement(getManufacturerRequest)) {
            getStatement.setLong(1, id);
            ResultSet resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                Long idOfManufacturer = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                manufacturer = new Manufacturer(idOfManufacturer, name, country);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
        return manufacturer;
    }
    /*TODO Throwing RuntimeException is a bad idea because it unchecked exceptions.
       And do not transform exception to its parent without a need. It`s shrink information about exceptions.
     */
    @Override
    public List<Manufacturer> getAll() throws RuntimeException {
        List<Manufacturer> manufacturerList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 Statement getAllStatement = connection.createStatement()) {
            ResultSet resultSet = getAllStatement
                    .executeQuery(
                        "SELECT manufacturers.id, manufacturers.name, manufacturers.country"
                        + "  FROM manufacturers WHERE is_deleted = 0");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                Long id = resultSet.getObject("id", Long.class);
                Manufacturer manufacturer = new Manufacturer(id, name, country);
                manufacturerList.add(manufacturer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
        return manufacturerList;
    }
    //TODO Statement.RETURN_GENERATED_KEYS is redundant here.
    @Override
    public void delete(Long id) {
        String deleteRequest = "UPDATE manufacturers SET is_deleted = 1 where id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement deleteStatement =
                         connection.prepareStatement(
                                 deleteRequest, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
    }
    //TODO It will always return empty Manufacturer better return Manufacturer from input. And check is update was performed.
    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String updateRequest = "UPDATE manufacturers SET name = ?, country = ? WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement updateStatement =
                        connection.prepareStatement(
                                updateRequest, Statement.RETURN_GENERATED_KEYS)) {
            updateStatement.setString(1, manufacturer.getName());
            updateStatement.setString(2, manufacturer.getCountry());
            updateStatement.setLong(3, manufacturer.getId());
            updateStatement.executeUpdate();
            return new Manufacturer();
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
    }
}
