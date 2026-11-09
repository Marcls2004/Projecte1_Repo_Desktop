package com.projecte;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MapaController {

    @FXML private AnchorPane contenidorMapa;
    @FXML private Label lbOcupacio;

    // Zones de la sala (de moment, dades de test)
    private List<ZonaMapa> zones;

    @FXML
    private void initialize() {
        System.out.println("Mapa obert");

        zones = DadesTestMapa.crearZones();

        mostrarOcupacio();
        dibuixarMapa();
    }

    private void mostrarOcupacio() {
        String text = "|  ";
        for (ZonaMapa zona : zones) {
            text += zona.getNom() + ": " + zona.getOcupades()
                + "/" + zona.getAforament() + "  |  ";
        }
        lbOcupacio.setText(text);
    }

    // Dibuixa els seients de les zones numerades en una graella (fila, columna)
    private void dibuixarMapa() {
        GridPane graella = new GridPane();
        graella.setHgap(3); // espai entre columnes
        graella.setVgap(3); // espai entre files
        graella.setPadding(new Insets(15));
        graella.setStyle("-fx-background-color: #1a1a2e;"); // fons fosc = passadissos

        for (ZonaMapa zona : zones) {
            if (!zona.esNumerada()) {
                // Zona general: un bloc únic sota el mapa, amb nom i ocupació
                Label etiqueta = new Label(zona.getNom() + "  "
                        + zona.getOcupades() + "/" + zona.getAforament());
                etiqueta.setMaxWidth(Double.MAX_VALUE);
                etiqueta.setMinHeight(40);
                etiqueta.setStyle("-fx-background-color: " + zona.getColor()
                        + "; -fx-text-fill: white; -fx-alignment: center;");

                // columna 0, fila 11 (sota les 10 files de la platea), ocupa 30 columnes
                graella.add(etiqueta, 0, 11, 30, 1);
                continue;
            }

            for (int f = 1; f <= zona.getNumFiles(); f++) {
                for (int s = 1; s <= zona.getSeientsPerFila(); s++) {
                    Seient seient = zona.getSeient(f, s);

                    Button boto = new Button();
                    boto.setMinSize(20, 20);
                    boto.setMaxSize(20, 20);

                    if (seient.estaVenut()) {
                        boto.setText("X");
                        boto.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 0;");
                        
                        final ZonaMapa zonaFinal = zona;
                        final Seient seientFinal = seient;

                        boto.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                mostrarDetallVenda(zonaFinal, seientFinal);
                            }
                        });
                    } else {
                        boto.setStyle("-fx-background-color: " + zona.getColor() + ";");
                    }

                    // Posició dins la graella: on comença la zona + posició del seient
                    int columna = zona.getColumnaInici() + (s - 1);
                    int fila = zona.getFilaInici() + (f - 1);
                    graella.add(boto, columna, fila);
                }
            }
        }

        contenidorMapa.getChildren().add(graella);
    }

    @FXML
    private void tornar(ActionEvent event) {
        try {
            Node boto = (Node) event.getSource();
            Stage stage = (Stage) boto.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("No s'ha pogut tornar al llistat: " + e.getMessage());
        }
    }

    private void mostrarDetallVenda(ZonaMapa zona, Seient seient) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        
        alerta.setTitle("Detall de la venda");
        alerta.setHeaderText("Localitzador: " + seient.getLocalitzador());
        alerta.setContentText(
            "Zona: " + zona.getNom() + "\n" +
            "Fila: " + seient.getFila() + "   Seient: " + seient.getNumero() + "\n" +
            "Tipus d'entrada: " + seient.getTipusEntrades() + "\n" +
            "Comprador: " + seient.getComprador() + "\n" +
            "Correu: " + seient.getCorreu()
        );

        Stage stageAlerta = (Stage) alerta.getDialogPane().getScene().getWindow();
        InputStream icona = getClass().getResourceAsStream("/icons/icon.png");
        if (icona != null) {
            stageAlerta.getIcons().add(new Image(icona));
        }
        alerta.showAndWait();
    }
}