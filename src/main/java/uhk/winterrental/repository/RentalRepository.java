package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
