package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uhk.winterrental.entity.Customer;
import uhk.winterrental.service.CustomerService;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * Redirects to the profile page of the customer
     * @param email Email of the customer
     * @param model Model to hold customer data
     * @return returns the profile page
     */
    @GetMapping(value = "/profile/{email}")
    public String profilePage(@PathVariable String email, Model model) {
        Customer customer = customerService.findCustomerByEmail(email);
        model.addAttribute("customer", customer);
        model.addAttribute("reservations", customer.getReservations());
        model.addAttribute("rentals", customer.getRentals());
        return "profile";
    }

    /**
     * Redirects to the registration page for new customers
     * @param model Model to hold customer data
     * @return returns the registration page
     */
    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "registration";
    }

    /**
     * Saves the registered customer data and redirects to the login page
     * @param customer Customer object containing registration data
     * @return Redirects to the login page after successful registration
     */
    @PostMapping(value = "/register")
    public String register(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/login";
    }

}
