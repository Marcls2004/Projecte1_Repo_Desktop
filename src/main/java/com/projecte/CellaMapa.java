package com.projecte;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

// Cel·la de la taula amb un botó "Mapa" que obre el mapa de l'esdeveniment d'aquella fila
public class CellaMapa extends TableCell<Esdeveniment, Void> {

    private Button boto = new Button("Mapa");

    public CellaMapa(final VendesController controlador) {
        boto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Esdeveniment de la fila on és aquest botó
                Esdeveniment esdeveniment = getTableView().getItems().get(getIndex());
                MapaController.idEsdeveniment = esdeveniment.getId();
                controlador.obrirMapa(event);
            }
        });
    }

    // La taula crida aquest mètode per pintar cada fila
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);   // fila buida: sense botó
        } else {
            setGraphic(boto);
        }
    }
}