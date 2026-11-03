package com.example.geomtria.controllers;


import com.example.geomtria.controllers.others.CanvasForm;
import com.example.geomtria.controllers.services.DibuixService;
import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CrearCanvasController {

    private final DibuixService dibuixService;
    private final JsonMapper jsonMapper;

    public CrearCanvasController(DibuixService dibuixService, JsonMapper jsonMapper) {
        this.dibuixService = dibuixService;
        this.jsonMapper = jsonMapper;
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

        model.addAttribute("dibuix", dibuix);
        return "editarDibuix";
    }

    @PostMapping("/dibuixos/{id}/guardar")
    public String guardarDibuix(@PathVariable("id") Long id, @RequestParam("drawing") String drawing, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Dibuix dibuix = dibuixService.cercarPerId(id);

        List<Figura> figures = jsonMapper.readValue(drawing, new TypeReference<List<Figura>>() {
        });

        dibuixService.desarFigures(dibuix, figures);
        return "redirect:/dibuixos/" + id + "/editar";

    }
}
