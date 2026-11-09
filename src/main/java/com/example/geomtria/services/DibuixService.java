package com.example.geomtria.services;


import com.example.geomtria.dto.DibuixPublic;
import com.example.geomtria.repos.DibuixRepo;
import com.example.geomtria.repos.UserRepo;
import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import com.example.geomtria.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;

@Service
public class DibuixService {

    private final DibuixRepo dibuixRepo;

    private final UserRepo userRepo;

    public DibuixService(DibuixRepo dibuixRepo, UserRepo userRepo) {
        this.dibuixRepo = dibuixRepo;
        this.userRepo = userRepo;
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

    public List<DibuixPublic> llistaPublica() {
        List<DibuixPublic> llistaCompleta = new ArrayList<>();

        for (Dibuix dibuix : dibuixRepo.findAll()) {
            String username = dibuix.getUsernamePropietari();
            User usuari = userRepo.findByUsername(username);

            String nomUsuari = usuari != null ? usuari.getName() : username;

            llistaCompleta.add(new DibuixPublic(dibuix, nomUsuari));
        }
        return llistaCompleta;
    }

    public Dibuix cercarPerId(Long id) {
        return dibuixRepo.findById(id);
    }

    public void desarFigures(Dibuix dibuix, List<Figura> figures) {
        dibuix.setFigures(figures);
        dibuix.setDataModificacio(LocalDateTime.now());
        dibuixRepo.save(dibuix);
    }
}
