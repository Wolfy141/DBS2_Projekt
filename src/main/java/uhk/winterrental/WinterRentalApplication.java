package uhk.winterrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uhk.winterrental.entity.Customer;

@SpringBootApplication
public class WinterRentalApplication {

    public static void main(String[] args) {
        Customer customer = new Customer();
        customer.setFirstName("John");
        SpringApplication.run(WinterRentalApplication.class, args);
    }

}
