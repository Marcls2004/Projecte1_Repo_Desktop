package com.projecte;

public class Seient {

    private int fila;
    private int numero;

    // Dades de la venda (null = el seient està lliure)
    private String comprador;
    private String correu;
    private String localitzador;
    private String tipusEntrada;

    public Seient(int fila, int numero) {
        this.fila = fila;
        this.numero = numero;
    }

    public boolean estaVenut() {
        return comprador != null;
    }

    public void vendre(String comprador, String correu, String localitzador, String tipusEntrada) {
        this.comprador = comprador;
        this.correu = correu;
        this.localitzador = localitzador;
        this.tipusEntrada = tipusEntrada;
    }

    public int getFila() { return fila; }
    public int getNumero() { return numero; }
    public String getComprador() { return comprador; }
    public String getCorreu() { return correu; }
    public String getLocalitzador() { return localitzador; }
    public String getTipusEntrades() { return tipusEntrada; }
}