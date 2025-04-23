package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uhk.winterrental.entity.Rental;
import uhk.winterrental.repository.RentalRepository;

import java.util.List;
import java.util.Objects;

@Controller
public class RentalController {
    @Autowired
    private RentalRepository rentalRepository;

    @GetMapping(value = "/rentals/{id}")
    public String showReservations(Model model, @PathVariable Long id) {
        List <Rental> rentals = rentalRepository.findAll();
        rentals.removeIf(rental -> !Objects.equals(rental.getCustomer().getId(), id));
        model.addAttribute("rentals", rentals);
        return "rented-equipment";
    }
}
