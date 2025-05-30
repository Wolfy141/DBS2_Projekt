package uhk.winterrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uhk.winterrental.entity.Customer;
import uhk.winterrental.entity.Employee;
import uhk.winterrental.entity.User;
import uhk.winterrental.repository.CustomerRepository;
import uhk.winterrental.repository.EmployeeRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    /**
     * Loads user details by email, checking first in the employee repository
     * @param email the email address of the user to load
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check employee repository first
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            return createUserDetails(employee.get());
        }

        // If not an employee, check customer repository
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return createUserDetails(customer.get());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    /**
     * Creates UserDetails object from a User entity.
     * @param user the user entity (Employee or Customer)
     * @return UserDetails object with user's email, password, and role
     */
    private UserDetails createUserDetails(User user) {
        String role = (user instanceof Employee) ? "ROLE_ADMIN" : "ROLE_CUSTOMER";
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    // Returns authority of the user based on their type
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String role = (user instanceof Employee) ? "ROLE_ADMIN" : "ROLE_CUSTOMER";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
