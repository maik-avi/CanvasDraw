package com.example.geomtria.controllers;


import com.example.geomtria.others.RegisterForm;
import com.example.geomtria.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    LoginService loginService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid @ModelAttribute("registerForm") RegisterForm registerForm,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        boolean created = loginService.register(
                registerForm.getName(),
                registerForm.getUsername(),
                registerForm.getPassword()
        );

        if (!created) {
            model.addAttribute("message", "Aquest usuari ja existeix");
            return "register";
        }

        return "redirect:/login";
    }
}
