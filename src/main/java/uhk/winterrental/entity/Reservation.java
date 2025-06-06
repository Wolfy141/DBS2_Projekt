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
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_equipment",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> equipment = new HashSet<>();

    // Adds equipment to the reservation, marking it as unavailable
    public void addEquipment(Equipment equipment) {
        equipment.setAvailable(false);
        this.equipment.add(equipment);
        equipment.getReservations().add(this);
    }

    // Returns the total cost of all equipment in the reservation
    public double getCostOfEquipment() {
        return equipment.stream()
                .mapToDouble(Equipment::getCostPerDay)
                .sum();
    }

    // Returns a comma-separated string of equipment names in the reservation
    public String getEquipmentNames() {
        return equipment.stream()
                .map(Equipment::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    // Checks if the reservation is still valid based on the expiration date
    public boolean isValid() {
        return expirationDate.isAfter(LocalDateTime.now());
    }
}
