package uhk.winterrental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uhk.winterrental.entity.Customer;
import uhk.winterrental.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a customer to the repository after encoding their password.
     * @param customer the customer to save
     */
    public void saveCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }

    /**
     * Finds a customer by their email address.
     * @param email the email address to search for
     * @return the customer with the given email, or null if not found
     */
    public Customer findCustomerByEmail(String email) {
        for (Customer customer : customerRepository.findAll()) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Saves a customer to the repository, encoding their password.
     * @param customer the customer to save
     */
    public void save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }
}
