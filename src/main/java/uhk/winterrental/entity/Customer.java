package uhk.winterrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, length = 32, unique = true)
    private String email;

    @Column(name = "first_name", length = 24)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 24)
    private String lastName;

    @Column(length = 20)
    private String phone;

    private String password;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();
}