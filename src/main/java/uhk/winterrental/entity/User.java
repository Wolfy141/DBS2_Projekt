package uhk.winterrental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // Superclass for all user types (e.g., Customer, employee)

    @Column(nullable = false, length = 32, unique = true)
    private String email;

    private String password;
}
