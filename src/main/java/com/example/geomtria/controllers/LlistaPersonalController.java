package com.example.geomtria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LlistaPersonalController {

    @GetMapping("/llistaPersonal")
    public String llistaPersonal() {
        return "llistaPersonal";
    }
}
