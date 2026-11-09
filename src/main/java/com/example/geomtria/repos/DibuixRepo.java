package com.example.geomtria.repos;


import com.example.geomtria.models.Dibuix;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface DibuixRepo {

    void save(Dibuix dibuix);

    Dibuix findById(Long id);

    List<Dibuix> findByUsernamePropietari(String username);

    List<Dibuix> findAll();

    void deleteById(Long id);
}
