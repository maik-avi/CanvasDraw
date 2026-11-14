package com.example.geomtria.controllers;


import com.example.geomtria.others.CanvasForm;
import com.example.geomtria.services.DibuixService;
import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Controller
public class CrearCanvasController {

    @Autowired
    DibuixService dibuixService;

    @Autowired
    JsonMapper jsonMapper;


    @GetMapping("/crearCanvas")
    public String crearCanvas(HttpSession session, Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("canvasForm", new CanvasForm());
        return "crearCanvas";
    }

    @PostMapping("/crearCanvas")
    public String crearCanvas(@Valid @ModelAttribute("canvasForm")CanvasForm form,
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

}
