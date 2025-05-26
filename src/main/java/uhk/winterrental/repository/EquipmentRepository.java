package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
