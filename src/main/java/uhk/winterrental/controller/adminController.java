package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uhk.winterrental.entity.Equipment;
import uhk.winterrental.repository.EquipmentCategoryRepository;
import uhk.winterrental.repository.EquipmentConditionRepository;
import uhk.winterrental.repository.EquipmentRepository;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentCategoryRepository categoryRepository;

    @Autowired
    private EquipmentConditionRepository conditionRepository;

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("conditions", conditionRepository.findAll());
        model.addAttribute("equipment", new Equipment());
        return "admin";
    }

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
}
