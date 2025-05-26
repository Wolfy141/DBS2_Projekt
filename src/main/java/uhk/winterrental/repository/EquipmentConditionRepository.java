package uhk.winterrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uhk.winterrental.entity.EquipmentCondition;

public interface EquipmentConditionRepository extends JpaRepository <EquipmentCondition, Long> {
}
