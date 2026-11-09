package com.example.geomtria.repos;


import com.example.geomtria.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepoImpl implements UserRepo{

    public UserRepoImpl() {
        save(new User("Bill", "bill", "12345"));
        save(new User("Tom", "tom", "abcde"));
        save(new User("miguel", "miguel", "olakease"));
    }

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }


}
