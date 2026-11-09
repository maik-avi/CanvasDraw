package com.example.geomtria.controllers;

import com.example.geomtria.services.DibuixService;
import com.example.geomtria.services.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LlistaPersonalController {


    @Autowired
    DibuixService dibuixService;


    @GetMapping("/llistaPersonal")
    public String llistaPersonal(HttpSession session, Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");
        model.addAttribute("dibuixos", dibuixService.llistaPersonals(username));
        return "llistaPersonal";
    }

    @PostMapping("/dibuixos/{id}/eliminar")
    public String eliminarDibuix(@PathVariable("id") Long id, HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        dibuixService.eliminarDibuix(id, username);
        return "redirect:/llistaPersonal";
    }
}
