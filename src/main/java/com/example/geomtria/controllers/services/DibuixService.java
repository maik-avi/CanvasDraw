package com.example.geomtria.controllers.services;


import com.example.geomtria.controllers.repos.DibuixRepo;
import com.example.geomtria.models.Dibuix;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DibuixService {

    private final DibuixRepo dibuixRepo;

    public DibuixService(DibuixRepo dibuixRepo) {
        this.dibuixRepo = dibuixRepo;
    }

    public Dibuix crearDibuix(String nom, String username, int amplada, int alcada) {
        if (amplada <= 0 || alcada <= 0) {
            throw new IllegalArgumentException("Les dimensions han de ser positives");
        }

        String nomFinal = nom == null ? "" : nom.trim();

        if (nomFinal.isEmpty()) {
            UUID identificador = UUID.randomUUID();
            String text = identificador.toString();
            String codiCurt = text.substring(0, 8);

            nomFinal = "Dibuix-" + codiCurt;
        }

        Dibuix dibuix = new Dibuix(nomFinal, username, amplada, alcada);
        dibuixRepo.save(dibuix);

        return dibuix;
    }

    public List<Dibuix> llistaPersonals(String username) {
        return dibuixRepo.findByUsernamePropietari(username);
    }
}
