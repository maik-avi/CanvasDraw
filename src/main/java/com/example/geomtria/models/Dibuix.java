package com.example.geomtria.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Dibuix {
    private Long id;
    private String nom;
    private String usernamePropietari;
    private LocalDateTime dataCreacio;
    private LocalDateTime dataModificacio;
    private int amplada;
    private int alcada;

    private List<Figura> figures = new ArrayList<>();

    public Dibuix(String nom, String usernamePropietari,int amplada, int alcada) {
        this.nom = nom;
        this.usernamePropietari = usernamePropietari;
        this.amplada = amplada;
        this.alcada = alcada;

        this.dataCreacio = LocalDateTime.now();
        this.dataModificacio = this.dataCreacio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsernamePropietari() {
        return usernamePropietari;
    }

    public void setUsernamePropietari(String usernamePropietari) {
        this.usernamePropietari = usernamePropietari;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public LocalDateTime getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(LocalDateTime dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public int getAmplada() {
        return amplada;
    }

    public void setAmplada(int amplada) {
        this.amplada = amplada;
    }

    public int getAlcada() {
        return alcada;
    }

    public void setAlcada(int alcada) {
        this.alcada = alcada;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
