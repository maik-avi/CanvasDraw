package com.example.geomtria.services;


import com.example.geomtria.repos.UserRepo;
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

        if (u == null) return false;

        return (password.equals(u.getPassword()));
    }

    public boolean register(String name, String username, String password) {

        User existingUser = userRepo.findByUsername(username);

        if (existingUser != null) {
            return false;
        }

        User newUser = new User(name, username, password);

        userRepo.save(newUser);
        return true;
    }
}
