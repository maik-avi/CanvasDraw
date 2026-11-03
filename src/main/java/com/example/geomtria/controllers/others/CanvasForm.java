package com.example.geomtria.controllers.others;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CanvasForm {

    private String nom;

    @NotNull(message = "Indica l'amplada")
    @Positive(message = "L'amplada ha de ser positiva")
    private Integer amplada = 800;

    @NotNull(message = "Indica l'alçada")
    @Positive(message = "L'alçcada ha de ser positiva")
    private Integer alcada = 600;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getAmplada() {
        return amplada;
    }

    public void setAmplada(Integer amplada) {
        this.amplada = amplada;
    }

    public Integer getAlcada() {
        return alcada;
    }

    public void setAlcada(Integer alcada) {
        this.alcada = alcada;
    }
}
