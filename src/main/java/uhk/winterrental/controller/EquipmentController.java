package uhk.winterrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uhk.winterrental.entity.Equipment;
import uhk.winterrental.repository.CategoryRepository;
import uhk.winterrental.repository.EquipmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping()
    public String listAll(Model model) {
        model.addAttribute("equipment", equipmentRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "equipment";
    }

    @GetMapping("/category/{id}")
    public String listByCategory(@PathVariable Long id, Model model) {
        List<Equipment> allEquipment = equipmentRepository.findAll();

        List<Equipment> filteredEquipment = allEquipment.stream()
                .filter(e -> e.getCategory().getId().equals(id))
                .collect(Collectors.toList());

        model.addAttribute("equipment", filteredEquipment);
        model.addAttribute("categories", categoryRepository.findAll());
        return "equipment";
    }

}
