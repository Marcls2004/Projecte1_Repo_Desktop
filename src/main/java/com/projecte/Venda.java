package com.projecte;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Venda {
    private final StringProperty id;
    private final StringProperty dataHora;
    private final StringProperty espectacle;
    private final StringProperty importVenda;
    private final StringProperty estat;
    private final StringProperty accions;

    public Venda(String id, String dataHora, String espectacle, String importVenda, String estat) {
        this.id = new SimpleStringProperty(id);
        this.dataHora = new SimpleStringProperty(dataHora);
        this.espectacle = new SimpleStringProperty(espectacle);
        this.importVenda = new SimpleStringProperty(importVenda);
        this.estat = new SimpleStringProperty(estat);
        this.accions = new SimpleStringProperty("[Detall]");
    }

    public StringProperty idProperty() { return id; }
    public StringProperty dataHoraProperty() { return dataHora; }
    public StringProperty espectacleProperty() { return espectacle; }
    public StringProperty importVendaProperty() { return importVenda; }
    public StringProperty estatProperty() { return estat; }
    public StringProperty accionsProperty() { return accions; }
}
