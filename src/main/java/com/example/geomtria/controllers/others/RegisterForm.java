package com.example.geomtria.controllers.others;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "El nom és obligatori")
    private String name;

    @NotBlank(message = "L'usuari és obligatori")
    @Size(min = 2, max = 10, message = "Entre 2 i 10 caràcters")
    private String username;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(min = 5, max = 10, message = "Entre 5 i 10 caràcters")
    private String password;

    // Genera los getters y setters de los tres atributos.

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
