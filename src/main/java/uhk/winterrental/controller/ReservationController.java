package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import uhk.winterrental.repository.CustomerRepository;
import uhk.winterrental.repository.EquipmentRepository;
import uhk.winterrental.repository.ReservationRepository;

@Controller
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/reservations")
    public String reservations(Model model) {
        //TODO: get reserved equipment from vybaveni
        //model.addAttribute("reservations", );
        return "reservations";
    }
    /*
    @PostMapping("/reserve/{id}")
    public String reserveEquipment(Model model, @PathVariable Long id) {
        Reservation reservation = new Reservation();
        reservation.addEquipment(equipmentRepository.findById(id).orElseThrow());
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpirationDate(LocalDateTime.now().plusDays(7));
        
        //TODO: get customer id from session
        reservation.setCustomer(customerRepository.findById((long)1).orElseThrow());
        model.addAttribute("reservation", reservationRepository.save(reservation));
        return "redirect:/";
    }

    @GetMapping(value = "/reservations/{id}")
    public String showReservations(Model model, @PathVariable Long id) {
        List <Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().getId() != id) {
                reservations.remove(reservation);
            }
        }
        model.addAttribute("reservations", reservations);
        return "";
    }

    @PostMapping("/cancel-reservation")
    public String removeReservation(@RequestParam Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        for (Equipment e : reservation.getEquipment()) {
            e.setAvailable(true);
            equipmentRepository.save(e);
        }
        reservationRepository.delete(reservation);
        // TODO: Redirect to the appropriate page (e.g., reservations list for the customer)
        return "redirect:/";
    }
    */
}
