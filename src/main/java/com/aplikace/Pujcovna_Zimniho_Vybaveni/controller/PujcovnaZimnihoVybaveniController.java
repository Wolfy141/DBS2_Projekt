package com.aplikace.Pujcovna_Zimniho_Vybaveni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.aplikace.Pujcovna_Zimniho_Vybaveni.model.Item;
import com.aplikace.Pujcovna_Zimniho_Vybaveni.service.ItemService;


@Controller
public class PujcovnaZimnihoVybaveniController {

@GetMapping("/index")
public String home() {
    return "index";
}

@GetMapping("/pujcovna")
public String pujcovna() {
    return "pujcovna";
}

@GetMapping("/kontakt")
public String kontakt() {
    return "kontakt";
}

@GetMapping("/profil")
public String profil() {
    return "profil";
}

@GetMapping("/prihlaseni")
public String prihlaseni() {
    return "prihlaseni";
}

@Autowired
private ItemService itemService;

@GetMapping("/vybaveni")
public String vybaveni(Model model) {
    List<Item> items = itemService.getAllItems();
    model.addAttribute("items", items);
    return "vybaveni";
}


}
