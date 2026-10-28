package com.example.geomtria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LlistaPublicaController {

    @GetMapping("/llistaPublica")
    public String llistaPublica() {
        return "llistaPublica";
    }

}
