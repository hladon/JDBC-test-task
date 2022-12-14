package mate.jdbc;

import mate.jdbc.dao.ManufacturerDao;
import mate.jdbc.dao.ManufacturerDaoImpl;
import mate.jdbc.model.Manufacturer;

public class Main {

    public static void main(String[] args) {
        ManufacturerDao manufacturerDao = new ManufacturerDaoImpl();
        Manufacturer manufacturer1 = new Manufacturer(null, "Lincoln", "USA");
        Manufacturer manufacturer2 = new Manufacturer(null, "Ford", "USA");
        Manufacturer manufacturer3 = new Manufacturer(null, "Audi", "Germany");
        Manufacturer manufacturer4 = new Manufacturer(null, "BMW", "Germany");
        manufacturerDao.create(manufacturer1);
        manufacturerDao.create(manufacturer2);
        manufacturerDao.create(manufacturer3);
        manufacturerDao.create(manufacturer4);

        manufacturerDao.delete(16L);
        manufacturerDao.delete(22L);
        manufacturerDao.delete(24L);
        manufacturerDao.delete(27L);

        System.out.println(manufacturerDao.get(23L));
        manufacturerDao.update(new Manufacturer(23L, "Volkswagen", "Germany"));
        System.out.println(manufacturerDao.get(23L));

    }
}
