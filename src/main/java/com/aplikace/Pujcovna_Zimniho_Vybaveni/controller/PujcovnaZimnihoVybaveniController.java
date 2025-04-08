package com.aplikace.Pujcovna_Zimniho_Vybaveni.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PujcovnaZimnihoVybaveniController {

    @GetMapping("/index")
public String home() {
    return "index";
}

@GetMapping("/vybaveni")
public String vybaveni() {
    return "vybaveni";
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

}
