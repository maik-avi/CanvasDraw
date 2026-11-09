package com.example.geomtria.controllers;

import com.example.geomtria.services.DibuixService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LlistaPublicaController {


    @Autowired
    DibuixService dibuixService;

    @GetMapping("/llistaPublica")
    public String llistaPublica(HttpSession session, Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("dibuixosPublics", dibuixService.llistaPublica());
        return "llistaPublica";
    }

}
