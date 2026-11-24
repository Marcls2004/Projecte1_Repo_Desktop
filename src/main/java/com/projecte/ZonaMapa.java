package com.projecte;

public class ZonaMapa {

    private String nom;
    private String tipus; // "numerada" o "general"
    private String color; 
    private int filaInici;
    private int columnaInici;
    private int numFiles;
    private int seientsPerFila;
    private int aforament;

    private Seient[][] seients; // només per a les numerades
    private int entradesVenudes; // només per a les generals

    // Constructor per a zones NUMERADES
    public ZonaMapa(String nom, String color, int filaInici, int columnaInici,
                    int numFiles, int seientsPerFila) {
        this.nom = nom;
        this.tipus = "numerada";
        this.color = color;
        this.filaInici = filaInici;
        this.columnaInici = columnaInici;
        this.numFiles = numFiles;
        this.seientsPerFila = seientsPerFila;
        this.aforament = numFiles * seientsPerFila;

        // Creem la matriu amb tots els seients lliures
        seients = new Seient[numFiles][seientsPerFila];
        for (int f = 0; f < numFiles; f++) {
            for (int s = 0; s < seientsPerFila; s++) {
                seients[f][s] = new Seient(f + 1, s + 1);
            }
        }
    }

    // Constructor per a zones GENERALS (sense seients)
    public ZonaMapa(String nom, String color, int aforament, int entradesVenudes) {
        this.nom = nom;
        this.tipus = "general";
        this.color = color;
        this.aforament = aforament;
        this.entradesVenudes = entradesVenudes;
    }

    public boolean esNumerada() {
        return tipus.equals("numerada");
    }

    // fila i numero comencen per 1 (com ho compten les persones)
    public Seient getSeient(int fila, int numero) {
        return seients[fila - 1][numero - 1];
    }

    // Quantes places ocupades té la zona
    public int getOcupades() {
        if (!esNumerada()) {
            return entradesVenudes;
        }
        int total = 0;
        for (int f = 0; f < numFiles; f++) {
            for (int s = 0; s < seientsPerFila; s++) {
                if (seients[f][s].estaVenut()) {
                    total++;
                }
            }
        }
        return total;
    }

    public String getNom() { return nom; }
    public String getTipus() { return tipus; }
    public String getColor() { return color; }
    public int getFilaInici() { return filaInici; }
    public int getColumnaInici() { return columnaInici; }
    public int getNumFiles() { return numFiles; }
    public int getSeientsPerFila() { return seientsPerFila; }
    public int getAforament() { return aforament; }
}