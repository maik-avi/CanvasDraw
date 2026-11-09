package com.example.geomtria.repos;

import com.example.geomtria.models.Dibuix;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.*;

@Repository
public class DibuixRepoImpl implements DibuixRepo{

    private Map<Long, Dibuix> dibuixos = new HashMap<>();

    Long sigueinteId = 1L;

    @Override
    public void save(Dibuix dibuix) {
        if (dibuix.getId() == null) {

            dibuix.setId(sigueinteId);
            sigueinteId++;

        }
        dibuixos.put(dibuix.getId(), dibuix);
    }

    @Override
    public Dibuix findById(Long id) {
        return dibuixos.get(id);
    }

    @Override
    public List<Dibuix> findByUsernamePropietari(String username) {
        return dibuixos.values().stream()
                .filter(dibuix ->
                        username.equals(dibuix.getUsernamePropietari()))
                .toList();
    }

    @Override
    public List<Dibuix> findAll() {
        return new ArrayList<>(dibuixos.values());
    }
}
