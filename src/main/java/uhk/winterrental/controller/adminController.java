package uhk.winterrental.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uhk.winterrental.entity.Customer;
import uhk.winterrental.entity.Equipment;
import uhk.winterrental.entity.Reservation;
import uhk.winterrental.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentCategoryRepository categoryRepository;

    @Autowired
    private EquipmentConditionRepository conditionRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Admin controller for managing equipment, categories, and customers.
     * Provides endpoints for adding equipment, listing categories, and handling rentals.
     */
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("conditions", conditionRepository.findAll());
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("customers", customerRepository.findAll());
        return "admin";
    }

    /**
     * Endpoint to add new equipment.
     * @param equipment The equipment to be added.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirects to the admin page with success or error message.
     */
    @PostMapping("/equipment/add")
    public String addEquipment(@ModelAttribute Equipment equipment,
                               RedirectAttributes redirectAttributes) {
        try {
            equipment.setAvailable(true); // Default to available
            equipmentRepository.save(equipment);
            redirectAttributes.addFlashAttribute("success", "Vybavení úspěšně přidáno");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Chyba při ukládání: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    /**
     * redirects to the rent equipment page for a specific customer.
     * @param id The ID of the equipment to be deleted.
     * @param model Model to add attributes for the view.
     * @param session Http session
     */
    @GetMapping("/customer/{id}")
    public String rentCustomerEquipment(@PathVariable Long id, Model model, HttpSession session) {
        session.setAttribute("customer_id", id);
        return addModelAttributes(model, session);
    }

    /**
     * redirects to the return equipment page for a specific customer.
     * @param id The ID of the customer whose equipment is being returned.
     * @param model Model to add attributes for the view.
     */
    @GetMapping("/return/{id}")
    public String returnCustomerEquipment(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        model.addAttribute("rentals", customer.getRentals());
        return "returnEquipment";
    }

    // Lists all equipment in the rent equipment page
    @GetMapping("/category-all")
    public String listAll(Model model, HttpSession session) {
        return addModelAttributes(model, session);
    }
    // Lists equipment based on category in the rent equipment page
    @GetMapping("/category/{category_id}")
    public String listByCategory(@PathVariable Long category_id, Model model, HttpSession session) {
        List<Equipment> allEquipment = equipmentRepository.findAll();

        List<Equipment> filteredEquipment = allEquipment.stream()
                .filter(e -> e.getCategory().getId().equals(category_id))
                .collect(Collectors.toList());

        addModelAttributes(model, session);
        model.addAttribute("equipment", filteredEquipment);
        return "rentEquipment";
    }

    // Adds model attributes for the rent equipment page.
    public String addModelAttributes(Model model, HttpSession session) {
        List<Equipment> allEquipment = equipmentRepository.findAll();
        model.addAttribute("equipment", allEquipment);
        model.addAttribute("categories", categoryRepository.findAll());
        List<Reservation> reservations = reservationRepository.findAll();
        List<Reservation> customerReservations = reservations.stream()
                .filter(r -> r.getCustomer().getId().equals(session.getAttribute("customer_id")))
                .collect(Collectors.toList());
        model.addAttribute("reservations", customerReservations);
        return "rentEquipment";
    }
}
