package uhk.winterrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long id;

    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate;

    @Column(nullable = false)
    private boolean returned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rental_equipment",
            joinColumns = @JoinColumn(name = "rental_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> equipment = new HashSet<>();

    public double getCostOfEquipment() {
        return equipment.stream()
                .mapToDouble(Equipment::getCostPerDay)
                .sum();
    }
    public String getEquipmentNames() {
        return equipment.stream()
                .map(Equipment::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
    public void addEquipment(Equipment equipment) {
        equipment.setAvailable(false);
        this.equipment.add(equipment);
        equipment.getRentals().add(this);
    }
}