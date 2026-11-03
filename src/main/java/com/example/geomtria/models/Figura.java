package com.example.geomtria.models;

public class Figura {

    private Long id;
    private String nom;
    private String tipus;
    private String color;
    private double x;
    private double y;
    private double mida;

    public Figura(String nom, String tipus, String color, double x, double y, double mida) {

        this.nom = nom;
        this.tipus = tipus;
        this.color = color;
        this.x = x;
        this.y = y;
        this.mida = mida;
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


}