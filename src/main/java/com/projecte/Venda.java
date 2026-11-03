package com.projecte;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Venda {
    private final StringProperty id;
    private final StringProperty dataHora;
    private final StringProperty espectacle;
    private final StringProperty importVenda;

    public Venda(String id, String dataHora, String espectacle, String importVenda) {
        this.id = new SimpleStringProperty(id);
        this.dataHora = new SimpleStringProperty(dataHora);
        this.espectacle = new SimpleStringProperty(espectacle);
        this.importVenda = new SimpleStringProperty(importVenda);
    }

    public StringProperty idProperty() { return id; }
    public StringProperty dataHoraProperty() { return dataHora; }
    public StringProperty espectacleProperty() { return espectacle; }
    public StringProperty importVendaProperty() { return importVenda; }
}
