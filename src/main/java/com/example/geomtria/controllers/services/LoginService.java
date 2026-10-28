package com.example.geomtria.controllers.services;


import com.example.geomtria.controllers.repos.UserRepo;
import com.example.geomtria.models.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepo userRepo;

    public LoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean checkLogin(String username, String password) {
        User u = userRepo.findByUsername(username);

        if ( u == null) return false;
        return (password.equals(u.getPassword()));
    }
}
