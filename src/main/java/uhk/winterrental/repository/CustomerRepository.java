package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
