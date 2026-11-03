package com.example.geomtria.controllers;

import com.example.geomtria.controllers.services.DibuixService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LlistaPersonalController {



    private final DibuixService dibuixService;

    public LlistaPersonalController(DibuixService dibuixService) {
        this.dibuixService = dibuixService;
    }


    @GetMapping("/llistaPersonal")
    public String llistaPersonal(HttpSession session, Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");
        model.addAttribute("dibuixos", dibuixService.llistaPersonals(username));
        return "llistaPersonal";
    }
}
