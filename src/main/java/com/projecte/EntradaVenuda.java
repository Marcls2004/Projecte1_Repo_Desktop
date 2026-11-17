package com.projecte;

// Una fila del llistat d'entrades venudes
public record EntradaVenuda(
        String dataHora,
        String localitzador,
        String tipus,
        String seient,
        String comprador,
        String estat) {
}