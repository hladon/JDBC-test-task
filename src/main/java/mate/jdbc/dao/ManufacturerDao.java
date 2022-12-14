package mate.jdbc.dao;

import java.util.List;
import mate.jdbc.model.Manufacturer;

public interface ManufacturerDao {
    Manufacturer create(Manufacturer manufacturer);

    Manufacturer get(Long id);

    List<Manufacturer> getAll() throws RuntimeException;

    Manufacturer update(Manufacturer manufacturer);

    void delete(Long id);
}
