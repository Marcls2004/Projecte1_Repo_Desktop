
package com.projecte;

import java.util.List;

public class Esdeveniment {

    private String id;
    private String nom;
    private String dataHora;
    private String categoria;
    private String descripcio;
    private List<TipusEntrada> tipusEntrades;

    public Esdeveniment(String id, String nom, String dataHora,
                        String categoria, String descripcio,
                        List<TipusEntrada> tipusEntrades) {
        this.id = id;
        this.nom = nom;
        this.dataHora = dataHora;
        this.categoria = categoria;
        this.descripcio = descripcio;
        this.tipusEntrades = tipusEntrades;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public List<TipusEntrada> getTipusEntrades() {
        return tipusEntrades;
    }
}
