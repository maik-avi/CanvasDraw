package com.example.geomtria.controllers;


import com.example.geomtria.controllers.others.CanvasForm;
import com.example.geomtria.controllers.services.DibuixService;
import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class CrearCanvasController {

    private final DibuixService dibuixService;

    public CrearCanvasController(DibuixService dibuixService) {
        this.dibuixService = dibuixService;
    }

    @GetMapping("/crearCanvas")
    public String crearCanvas(HttpSession session, Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("canvasForm", new CanvasForm());
        return "crearCanvas";
    }

    @PostMapping("/crearCanvas")
    public String crearCanvas(@Valid @ModelAttribute("canvasForm")Dibuix form,
            BindingResult result, HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "crearCanvas";
        }

        Dibuix dibuix = dibuixService.crearDibuix(
                form.getNom(), username, form.getAmplada(), form.getAlcada()
        );

        return "redirect:/dibuixos/" + dibuix.getId() + "/editar";
    }

    @GetMapping("/dibuixos/{id}/editar")
    public String editarDibuix(
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

        if (!username.equals(dibuix.getUsernamePropietari())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        model.addAttribute("dibuix", dibuix);
        return "editarDibuix";
    }
}
