package com.example.geomtria.controllers.others;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotBlank(message="Username es obligatori")
    @Size(min=2, max= 10, message = "Minim es 2")
    String username;


    @NotBlank(message="Password es obligatori")
    @Size(min=5, max= 10, message = "Password minim 5 caracters")
    String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String message) {
        this.username = message;
    }

}