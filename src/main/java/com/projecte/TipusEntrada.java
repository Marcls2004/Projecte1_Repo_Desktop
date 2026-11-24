
package com.projecte;

public class TipusEntrada {

    private String idTipus;
    private String nomTipus;
    private double preuTipus;
    private int quotaTipus;
    private String tipusZona;

    public TipusEntrada(String idTipus, String nomTipus,
                        double preuTipus, int quotaTipus,
                        String tipusZona) {
        this.idTipus = idTipus;
        this.nomTipus = nomTipus;
        this.preuTipus = preuTipus;
        this.quotaTipus = quotaTipus;
        this.tipusZona = tipusZona;
    }

    public String getIdTipus() {
        return idTipus;
    }

    public String getNomTipus() {
        return nomTipus;
    }

    public double getPreuTipus() {
        return preuTipus;
    }

    public int getQuotaTipus() {
        return quotaTipus;
    }

    public String getTipusZona() {
        return tipusZona;
    }
}
