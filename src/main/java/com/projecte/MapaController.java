package com.projecte;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
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

    // Esdeveniment del qual es mostra el mapa (el posa el botó "Mapa" del llistat)
    public static String idEsdeveniment;

    // Colors per a les zones, en ordre
    private static final String[] COLORS = {"#3498db", "#27ae60", "#e67e22", "#9b59b6", "#16a085", "#c0392b"};

    // Zones de la sala, carregades de la BD a través del servidor
    private List<ZonaMapa> zones = new ArrayList<>();

    @FXML
    private void initialize() {
        if (idEsdeveniment == null) {
            lbOcupacio.setText("Tria un esdeveniment del llistat amb el botó \"Mapa\"");
            return;
        }

        lbOcupacio.setText("Carregant el mapa...");

        // Cada cop que s'obre el mapa es demanen les dades actuals al servidor
        ClientWs.establirReceptor(new Consumer<String>() {
            @Override
            public void accept(String missatge) {
                rebreMissatge(missatge);
            }
        });
        ClientWs.enviar("{\"type\":\"getMapa\",\"idEsdeveniment\":\"" + idEsdeveniment + "\"}");
    }

    // Resposta del servidor: zones del recinte i entrades venudes de l'esdeveniment
    private void rebreMissatge(String missatge) {
        JSONObject resposta = new JSONObject(missatge);
        if (!"mapa".equals(resposta.optString("type"))) {
            return;
        }

        List<ZonaMapa> zonesNoves = new ArrayList<>();
        JSONArray jZones = resposta.getJSONArray("zones");

        for (int i = 0; i < jZones.length(); i++) {
            JSONObject jZona = jZones.getJSONObject(i);
            JSONArray venuts = jZona.getJSONArray("venuts");
            String color = COLORS[i % COLORS.length];

            if ("numerada".equals(jZona.getString("tipus"))) {
                ZonaMapa zona = new ZonaMapa(jZona.getString("nom"), color,
                        jZona.getInt("filaInici"), jZona.getInt("columnaInici"),
                        jZona.getInt("numFiles"), jZona.getInt("seientsPerFila"));

                // Marquem com a venuts els seients que tenen entrada
                for (int j = 0; j < venuts.length(); j++) {
                    JSONObject v = venuts.getJSONObject(j);
                    zona.getSeient(v.getInt("fila"), v.getInt("seient")).vendre(
                            v.getString("comprador"), v.getString("correu"),
                            v.getString("localitzador"), v.getString("tipus"));
                }
                zonesNoves.add(zona);
            } else {
                // Zona general: només aforament i entrades venudes
                zonesNoves.add(new ZonaMapa(jZona.getString("nom"), color,
                        jZona.getInt("aforament"), venuts.length()));
            }
        }

        zones = zonesNoves;
        final String titol = resposta.optString("recinte") + " - " + resposta.optString("esdeveniment");

        // El missatge arriba en un altre fil: la pantalla s'actualitza al fil de JavaFX
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mostrarOcupacio(titol);
                dibuixarMapa();
            }
        });
    }

    private void mostrarOcupacio(String titol) {
        String text = titol + "\n|  ";
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

        // Fins on arriben les zones numerades (per posar les generals a sota)
        int maxFila = 0;
        int maxColumna = 1;
        for (ZonaMapa zona : zones) {
            if (zona.esNumerada()) {
                maxFila = Math.max(maxFila, zona.getFilaInici() + zona.getNumFiles());
                maxColumna = Math.max(maxColumna, zona.getColumnaInici() + zona.getSeientsPerFila());
            }
        }
        int filaGeneral = maxFila + 1;

        for (ZonaMapa zona : zones) {
            if (!zona.esNumerada()) {
                // Zona general: un bloc únic sota el mapa, amb nom i ocupació
                Label etiqueta = new Label(zona.getNom() + "  "
                        + zona.getOcupades() + "/" + zona.getAforament());
                etiqueta.setMaxWidth(Double.MAX_VALUE);
                etiqueta.setMinHeight(40);
                etiqueta.setStyle("-fx-background-color: " + zona.getColor()
                        + "; -fx-text-fill: white; -fx-alignment: center;");

                // Just a sota de l'última fila i tan ample com la sala
                graella.add(etiqueta, 0, filaGeneral, maxColumna, 1);
                filaGeneral++;
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

        contenidorMapa.getChildren().setAll(graella);
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