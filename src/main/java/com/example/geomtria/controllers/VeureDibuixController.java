package com.example.geomtria.controllers;

import com.example.geomtria.models.Dibuix;
import com.example.geomtria.services.DibuixService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
@Controller
public class VeureDibuixController {

    @Autowired
    DibuixService dibuixService;

    @GetMapping("/dibuixos/{id}/veureDibuix")
    public String veureDibuix(
            @PathVariable("id") Long id,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Dibuix dibuix = dibuixService.cercarPerId(id);
        if (dibuix == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (username.equals(dibuix.getUsernamePropietari())) {
            return "redirect:/dibuixos/" + id + "/editar";
        }

        model.addAttribute("dibuix", dibuix);
        return "veureDibuix";
    }
}
