package ISP.Question2;

import java.sql.Driver;

public class Car implements Drivable {
    @Override
    public void drive() {
        System.out.println("Car driving");
    }
}
