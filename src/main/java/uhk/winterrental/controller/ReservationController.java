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

    @GetMapping("/reservation")
    public String reservationPage(Model model) {
        if(reservedEquipment.isEmpty()) {
            return "redirect:/equipment";
        }
        model.addAttribute("equipment", reservedEquipment);
        return "reservations";
    }

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
