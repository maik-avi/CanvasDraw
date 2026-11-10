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
            return "redirect:/dibuixos/" + dibuix.getId() + "/veureDibuix";
        }

        model.addAttribute("dibuix", dibuix);
        return "editarDibuix";
    }

    @PostMapping("/dibuixos/{id}/guardar")
    public String guardarDibuix(@PathVariable("id") Long id, @RequestParam("drawing") String drawing, HttpSession session, @RequestParam(name = "mostrar", defaultValue = "false") boolean mostrar) {

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

        List<Figura> figures = jsonMapper.readValue(drawing, new TypeReference<List<Figura>>() {
        });

        dibuixService.desarFigures(dibuix, figures, mostrar);

        System.out.println("Dibuix: " + dibuix.getNom());

        for (Figura figura : figures) {
            System.out.println("  ID: " + figura.getId());
            System.out.println("  Nom: " + figura.getNom());
            System.out.println("  Tipus: " + figura.getTipus());
            System.out.println("  Color: " + figura.getColor());
            System.out.println("  Mida: " + figura.getMida());
            System.out.println("  Posició: (" + figura.getX() + ", " + figura.getY() + ")");
            System.out.println("  Omplert: " + figura.getOmplert());
            System.out.println();
        }
        return "redirect:/dibuixos/" + id + "/editar";

    }
}
