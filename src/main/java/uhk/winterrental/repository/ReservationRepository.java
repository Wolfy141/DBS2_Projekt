package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
