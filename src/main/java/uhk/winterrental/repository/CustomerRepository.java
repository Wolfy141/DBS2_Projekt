package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}