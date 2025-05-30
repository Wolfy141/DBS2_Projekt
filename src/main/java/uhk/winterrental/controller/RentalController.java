package uhk.winterrental.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uhk.winterrental.entity.Equipment;
import uhk.winterrental.entity.Rental;
import uhk.winterrental.entity.Reservation;
import uhk.winterrental.repository.CustomerRepository;
import uhk.winterrental.repository.EquipmentRepository;
import uhk.winterrental.repository.RentalRepository;
import uhk.winterrental.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("rentedEquipment")
public class RentalController {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private List<Equipment> rentedEquipment = new ArrayList<>();

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/rent-reserved/{id}")
    public String rentReservedEquipment(@PathVariable Long id, HttpSession session) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
        reservation.setExpirationDate(LocalDateTime.now());
        for (Equipment equipment : reservation.getEquipment()) {
            equipment.setAvailable(false);
            equipmentRepository.save(equipment);
            rentedEquipment.add(equipment);
        }
        return "redirect:/admin/customer/" + session.getAttribute("customer_id");
    }

    @PostMapping("/rent/{id}")
    public String addToRental(@PathVariable Long id, HttpSession session) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
        rentedEquipment.add(equipment);

        return "redirect:/admin/customer/" + session.getAttribute("customer_id");
    }

    @GetMapping("/rental")
    public String rentalPage(Model model, HttpSession session) {
        if(rentedEquipment.isEmpty()) {
            return "redirect:/admin/customer/" + session.getAttribute("customer_id");
        }
        model.addAttribute("equipment", rentedEquipment);
        return "rental";
    }

    @PostMapping("/cancel-rental")
    public String cancelRental(@RequestParam("equipmentIds") List<Long> equipmentIds, SessionStatus sessionStatus, HttpSession session) {
        sessionStatus.setComplete();
        for (Long id : equipmentIds) {
            Equipment e = equipmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
            e.setAvailable(true);
            rentedEquipment.clear();
            equipmentRepository.save(e);
        }
        return "redirect:/admin/customer/" + session.getAttribute("customer_id");
    }

    @PostMapping("/finalize-rental")
    public String finalizeRental(
            HttpSession session,
            @RequestParam("dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
            @RequestParam("equipmentIds") List<Long> equipmentIds,
            SessionStatus sessionStatus) {

        // Create new rental
        Rental rental = new Rental();
        Long customerId = (Long) session.getAttribute("customer_id");
        rental.setCustomer(customerRepository.findById(customerId).orElseThrow());
        rental.setRentalDate(LocalDateTime.now());
        LocalDateTime returnDate = dateEnd.atTime(LocalTime.MAX); // End of day
        rental.setReturnDate(returnDate);

        // Add all equipment
        for (Long id : equipmentIds) {
            Equipment e = equipmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
            rental.addEquipment(e);
        }

        rentalRepository.save(rental);
        rentedEquipment.clear();
        sessionStatus.setComplete(); // Clear session attributes
        session.removeAttribute("customer_id");

        return "redirect:/admin";
    }
}
