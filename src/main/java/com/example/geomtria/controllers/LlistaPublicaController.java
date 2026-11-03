package com.example.geomtria.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LlistaPublicaController {

    @GetMapping("/llistaPublica")
    public String llistaPublica(HttpSession session) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        return "llistaPublica";
    }

}
