package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uhk.winterrental.entity.Equipment;
import uhk.winterrental.entity.Reservation;
import uhk.winterrental.repository.EquipmentRepository;
import uhk.winterrental.repository.ReservationRepository;
import uhk.winterrental.service.CustomerService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("reservedEquipment")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private List<Equipment> reservedEquipment = new ArrayList<>();

    @Autowired
    private CustomerService customerService;

    /**
     * Adds equipment to the reservation list.
     * @param id ID of the equipment to reserve
     * @param redirectAttributes Redirect attributes to pass messages
     * @return Redirect to the equipment list page
     */
    @PostMapping("/reserve/{id}")
    public String addToReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));

        if (!equipment.isAvailable()) {
            redirectAttributes.addFlashAttribute("error", "Equipment is not available");
            return "redirect:/equipment";
        }

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
        reservedEquipment.add(equipment);

        return "redirect:/equipment";
    }

    /**
     * Displays the reservation page with reserved equipment.
     * @param model Model to add attributes for the view
     * @return View name for reservations or redirect to equipment list if no equipment is reserved
     */
    @GetMapping("/reservation")
    public String reservationPage(Model model) {
        if(reservedEquipment.isEmpty()) {
            return "redirect:/equipment";
        }
        model.addAttribute("equipment", reservedEquipment);
        return "reservations";
    }

    /**
     * Removes equipment from the reservation list, sets it back to available
     * @param equipmentIds IDs of the equipment to remove, set available
     * @param sessionStatus Session status to clear session attributes
     * @return Redirect to the reservation page
     */
    @PostMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam("equipmentIds") List<Long> equipmentIds, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        for (Long id : equipmentIds) {
            Equipment e = equipmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
            e.setAvailable(true);
            reservedEquipment.clear();
            equipmentRepository.save(e);
        }
        return "redirect:/equipment";
    }

    /**
     * Finalizes the reservation by saving it to the database for the user.
     * @param dateEnd End date for the reservation
     * @param equipmentIds List of IDs of the reserved equipment
     * @param principal Principal object to get the logged-in user's email
     * @param sessionStatus Session status to clear session attributes
     * @return Redirects to the user's profile page after finalizing the reservation
     */
    @PostMapping("/finalize-reservation")
    public String finalizeReservation(
            @RequestParam("dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
            @RequestParam("equipmentIds") List<Long> equipmentIds,
            Principal principal,
            SessionStatus sessionStatus) {

        // Create new reservation
        Reservation reservation = new Reservation();
        reservation.setCustomer(customerService.findCustomerByEmail(principal.getName()));
        reservation.setReservationDate(LocalDateTime.now());
        LocalDateTime expirationDateTime = dateEnd.atTime(LocalTime.MAX); // End of day
        reservation.setExpirationDate(expirationDateTime);

        // Add all equipment
        for (Long id : equipmentIds) {
            Equipment e = equipmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid equipment ID"));
            reservation.addEquipment(e);
        }

        reservationRepository.save(reservation);
        reservedEquipment.clear();
        sessionStatus.setComplete(); // Clear session attributes

        return "redirect:/profile/" + principal.getName();
    }

}
