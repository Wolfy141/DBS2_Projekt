package uhk.winterrental.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
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

    /**
     * Rents reserved equipment.
     * @param id ID of the reservation
     * @param session HTTP session to retrieve customer ID
     * @return redirects to the customer's page after adding the equipment to the rental
     */
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

    /**
     * Adds unreserved equipment to the rental.
     * @param id ID of the equipment to be rented
     * @param session HTTP session to retrieve customer ID
     * @return redirects to the customer's page after adding the equipment to the rental
     */
    @PostMapping("/rent/{id}")
    public String addToRental(@PathVariable Long id, HttpSession session) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
        rentedEquipment.add(equipment);

        return "redirect:/admin/customer/" + session.getAttribute("customer_id");
    }

    /**
     * Displays the rental page with all rented equipment.
     * @param model Model to add attributes for the view
     * @param session HTTP session to check if there are rented equipment
     * @return "rental" view if there are rented equipment, otherwise redirects to the customer's page
     */
    @GetMapping("/rental")
    public String rentalPage(Model model, HttpSession session) {
        if(rentedEquipment.isEmpty()) {
            return "redirect:/admin/customer/" + session.getAttribute("customer_id");
        }
        model.addAttribute("equipment", rentedEquipment);
        return "rental";
    }

    /**
     * Cancels the rental of all rented equipment.
     * @param equipmentIds List of IDs of the equipment to be canceled
     * @param sessionStatus Session status to clear session attributes
     * @param session HTTP session to retrieve customer ID
     * @return redirects to the customer's page after canceling the rental
     */
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

    /**
     * Finalizes the rental by creating a new Rental entity and saving it to the database.
     * @param session HTTP session to retrieve customer ID
     * @param dateEnd End date of the rental
     * @param equipmentIds List of IDs of the equipment to be rented
     * @param sessionStatus Session status to clear session attributes
     * @return redirects to the admin page after finalizing the rental
     */
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

    /**
     * Returns rented equipment by updating the rental status and making the equipment available again.
     * @param id ID of the rental to be returned
     * @return redirects to the admin page after returning the equipment
     */
    @PostMapping("/return-rented/{id}")
    public String returnRentedEquipment(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
        rental.setReturnDate(LocalDateTime.now());
        for (Equipment equipment : rental.getEquipment()) {
            equipment.setAvailable(true);
            equipmentRepository.save(equipment);
        }
        rental.setReturned(true);
        rentalRepository.save(rental);
        return "redirect:/admin";
    }
}
