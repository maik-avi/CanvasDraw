package com.example.geomtria.controllers;

import com.example.geomtria.controllers.others.LoginForm;
import com.example.geomtria.controllers.others.RegisterForm;
import com.example.geomtria.controllers.services.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }


    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            System.out.println("Error-----------------");
            return "login";
        }

        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        if (loginService.checkLogin(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/crearCanvas";
        }

        model.addAttribute("message", "Usuari/pass incorrectes");
        return "login";
    }

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

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
