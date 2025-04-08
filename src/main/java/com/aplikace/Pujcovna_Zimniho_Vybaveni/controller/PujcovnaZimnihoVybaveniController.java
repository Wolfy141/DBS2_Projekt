package com.aplikace.Pujcovna_Zimniho_Vybaveni.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PujcovnaZimnihoVybaveniController {

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("title", "Hello World");
        return "index";
    }
}
