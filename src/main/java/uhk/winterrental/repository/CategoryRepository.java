package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.EquipmentCategory;

public interface CategoryRepository extends JpaRepository<EquipmentCategory, Long> {
}
