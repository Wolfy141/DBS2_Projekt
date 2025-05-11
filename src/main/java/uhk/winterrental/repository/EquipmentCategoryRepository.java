package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.EquipmentCategory;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
}
