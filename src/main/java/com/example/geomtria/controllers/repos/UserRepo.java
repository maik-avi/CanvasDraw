package com.example.geomtria.controllers.repos;

import com.example.geomtria.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface UserRepo {

    public void save(User user);

    User findByUsername(String username);
}
