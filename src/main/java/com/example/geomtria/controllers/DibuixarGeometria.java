package com.example.geomtria.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DibuixarGeometria {

    @GetMapping("/dibuixarGeometria")
    public String dibuixarGeometria() {
        return "dibuixarGeometria";
    }
}
