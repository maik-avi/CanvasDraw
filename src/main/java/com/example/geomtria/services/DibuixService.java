package com.example.geomtria.services;


import com.example.geomtria.dto.DibuixPublic;
import com.example.geomtria.repos.DibuixRepo;
import com.example.geomtria.repos.UserRepo;
import com.example.geomtria.models.Dibuix;
import com.example.geomtria.models.Figura;
import com.example.geomtria.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new IllegalArgumentException("Els parametres han de ser positius");
        }

        String nomFinal = nom;
        if (nomFinal == null || nomFinal.isEmpty()) {
            nomFinal = UUID.randomUUID().toString().substring(0, 8);
        }

        Dibuix dibuix = new Dibuix(nomFinal, username, amplada, alcada);

        dibuixRepo.save(dibuix);
        return dibuix;

    }

    public List<Dibuix> llistaPersonals(String username) {
        return dibuixRepo.findByUsernamePropietari(username);
    }

    public List<DibuixPublic> llistaPublica() {
        List<DibuixPublic> llistaPublica = new ArrayList<>();

        for (Dibuix dibuix : dibuixRepo.findAll()) {
            if (!dibuix.isMostrar()) {
                continue;
            }

            String username = dibuix.getUsernamePropietari();
            User user = userRepo.findByUsername(username);
            String nomUsuari = user.getName();

            llistaPublica.add(new DibuixPublic(dibuix,nomUsuari));
        }

        return llistaPublica;
    }

    public Dibuix cercarPerId(Long id) {
        return dibuixRepo.findById(id);
    }

    public void desarFigures(Dibuix dibuix, List<Figura> figures, boolean mostrar) {
        dibuix.setFigures(figures);
        dibuix.setMostrar(mostrar);
        dibuix.setDataModificacio(LocalDateTime.now());
        dibuixRepo.save(dibuix);
    }

    public void eliminarDibuix(Long id, String username) {
        Dibuix dibuix = dibuixRepo.findById(id);

        if (dibuix == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!username.equals(dibuix.getUsernamePropietari())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        dibuixRepo.deleteById(id);
    }
}
