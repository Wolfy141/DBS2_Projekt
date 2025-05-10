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
    public String loginPage() {
        return "loginOLD";
    }
    @GetMapping(value = "/register")
    public String create1(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "registrationOLD";
    }

    @PostMapping(value = "/register")
    public String create2(Model model, Customer customer) {
        model.addAttribute("customer", customerRepository.save(customer));
        return "redirect:/";
    }

    @GetMapping(value = "/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerRepository.findById(id).orElseThrow());
        return "edit-profile";
    }

}
