package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uhk.winterrental.entity.Customer;
import uhk.winterrental.repository.CustomerRepository;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "registration";
    }

    @PostMapping(value = "/register")
    public String register(Model model, Customer customer) {
        model.addAttribute("customer", customerRepository.save(customer));
        return "redirect:/";
    }

    @PostMapping(value = "/login")
    public String login(Model model, Customer customer) {
        for (Customer c : customerRepository.findAll()) {
            if (c.getEmail().equals(customer.getEmail()) && c.getPassword().equals(customer.getPassword())) {
                model.addAttribute("customer", c);
                return "redirect:/";
            }
        }
        return "redirect:/login";
    }

    /*@GetMapping(value = "/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerRepository.findById(id).orElseThrow());
        return "edit-profile";
    }*/

}
