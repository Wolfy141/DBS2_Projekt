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

    @GetMapping(value = "/profile/{email}")
    public String profilePage(@PathVariable String email, Model model) {
        Customer customer = customerService.findCustomerByEmail(email);
        model.addAttribute("customer", customer);
        model.addAttribute("reservations", customer.getReservations());
        model.addAttribute("rentals", customer.getRentals());
        return "profile";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "registration";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/login";
    }

}
