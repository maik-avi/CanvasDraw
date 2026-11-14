package com.example.geomtria.models;

import java.util.ArrayList;
import java.util.List;

public class Figura {

    private Long id;
    private String nom;
    private String tipus;
    private String color;
    private boolean omplert;
    private double x;
    private double y;
    private double mida;

    private List<Punt> punts = new ArrayList<>();


    public Figura(String nom, String tipus, String color, boolean omplert, double x, double y, double mida) {
        this.nom = nom;
        this.tipus = tipus;
        this.color = color;
        this.omplert = omplert;
        this.x = x;
        this.y = y;
        this.mida = mida;
    }

    public Figura() {

    }

    public boolean getOmplert() {
        return omplert;
    }

    public void setOmplert(boolean omplert) {
        this.omplert = omplert;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMida() {
        return mida;
    }

    public void setMida(double mida) {
        this.mida = mida;
    }

    public List<Punt> getPunts() {
        return punts;
    }

    public void setPunts(List<Punt> punts) {
        this.punts = punts;
    }

}