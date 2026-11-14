package com.example.geomtria.controllers;


import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import com.example.geomtria.services.DibuixService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Controller
public class EditarDibuixController {

    @Autowired
    DibuixService dibuixService;

    @Autowired
    JsonMapper jsonMapper;

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

        return "redirect:/dibuixos/" + id + "/editar";

    }
}
