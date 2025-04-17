package uhk.winterrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    private EquipmentCondition condition;

    private boolean available = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private EquipmentCategory category;

    @ManyToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    @ManyToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();
}