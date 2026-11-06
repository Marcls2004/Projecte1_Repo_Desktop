package com.projecte;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VendesController {

    @FXML private TableView<Esdeveniment> tablaVendes;
    @FXML private TableColumn<Esdeveniment, String> colId;
    @FXML private TableColumn<Esdeveniment, String> colData;
    @FXML private TableColumn<Esdeveniment, String> colEspectacle;
    @FXML private TableColumn<Esdeveniment, String> colImport;

    @FXML private MenuButton menuEspectacle;

    private ObservableList<Esdeveniment> llistaEsdeveniments =
            FXCollections.observableArrayList();

    private FilteredList<Esdeveniment> llistaFiltrada;

    @FXML
    public void initialize() {

        // Configuramos las columnas para mostrar acontecimientos.
        colId.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getId()));

        colData.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getDataHora().replace("T", " ")
                ));

        colEspectacle.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNom()));

        colImport.setText("Tipus d'entrada");

        colImport.setCellValueFactory(cell -> {
            StringBuilder text = new StringBuilder();

            for (TipusEntrada entrada :
                    cell.getValue().getTipusEntrades()) {

                if (text.length() > 0) {
                    text.append(" | ");
                }

                text.append(entrada.getNomTipus())
                    .append(": ")
                    .append(entrada.getPreuTipus())
                    .append(" €");
            }

            return new SimpleStringProperty(text.toString());
        });

        // Preparamos la lista y la tabla.
        llistaFiltrada = new FilteredList<>(
                llistaEsdeveniments, p -> true
        );

        tablaVendes.setItems(llistaFiltrada);

        // Escuchamos los mensajes del servidor.
        ClientWs.establirReceptor(this::rebreMissatge);

        // Solicitamos los acontecimientos.
        ClientWs.enviar("{\"type\":\"getEsdeveniments\"}");
    }

    private void rebreMissatge(String message) {

        try {
            JSONObject resposta = new JSONObject(message);

            if (!"esdeveniments".equals(
                    resposta.optString("type"))) {
                return;
            }

            JSONArray dades = resposta.getJSONArray("data");

            List<Esdeveniment> esdeveniments = new ArrayList<>();

            for (int i = 0; i < dades.length(); i++) {

                JSONObject jsonEsdeveniment =
                        dades.getJSONObject(i);

                JSONArray jsonEntrades =
                        jsonEsdeveniment.getJSONArray("tipusEntrades");

                List<TipusEntrada> entrades = new ArrayList<>();

                for (int j = 0; j < jsonEntrades.length(); j++) {

                    JSONObject jsonEntrada =
                            jsonEntrades.getJSONObject(j);

                    TipusEntrada entrada = new TipusEntrada(
                            jsonEntrada.getString("idTipus"),
                            jsonEntrada.getString("nomTipus"),
                            jsonEntrada.getDouble("preuTipus"),
                            jsonEntrada.getInt("quotaTipus"),
                            jsonEntrada.optString(
                                    "tipusZona", "general")
                    );

                    entrades.add(entrada);
                }

                Esdeveniment esdeveniment = new Esdeveniment(
                        jsonEsdeveniment.getString("id"),
                        jsonEsdeveniment.getString("nom"),
                        jsonEsdeveniment.getString("dataHora"),
                        jsonEsdeveniment.getString("categoria"),
                        jsonEsdeveniment.getString("descripcio"),
                        entrades
                );

                esdeveniments.add(esdeveniment);
            }

            Platform.runLater(() -> {

                llistaEsdeveniments.setAll(esdeveniments);

                // Actualizamos el filtro de acontecimientos.
                if (menuEspectacle != null) {

                    menuEspectacle.getItems().clear();

                    MenuItem tots = new MenuItem("Tots");
                    tots.setOnAction(e -> aplicarFiltro("Tots"));

                    menuEspectacle.getItems().add(tots);

                    for (Esdeveniment esdeveniment :
                            llistaEsdeveniments) {

                        MenuItem item = new MenuItem(
                                esdeveniment.getNom());

                        item.setOnAction(e ->
                                aplicarFiltro(esdeveniment.getNom()));

                        menuEspectacle.getItems().add(item);
                    }
                }

                System.out.println(
                        "Esdeveniments carregats: "
                        + llistaEsdeveniments.size());
            });

        } catch (Exception e) {
            System.out.println(
                    "Error processant els esdeveniments:");
            e.printStackTrace();
        }
    }

    private void aplicarFiltro(String nomEspectacle) {

        if ("Tots".equals(nomEspectacle)) {

            llistaFiltrada.setPredicate(esdeveniment -> true);

            menuEspectacle.setText("Espectacle: Tots");

        } else {

            llistaFiltrada.setPredicate(esdeveniment ->
                    esdeveniment.getNom().equals(nomEspectacle));

            menuEspectacle.setText(
                    "Espectacle: " + nomEspectacle);
        }
    }
    
    @FXML
    private void handleLlistatVendes(javafx.event.ActionEvent event) {
        System.out.println("[DEMO] Clic en Llistat de vendes - Cambiando a vista de lista");
        // Aquí puedes meter código para refrescar la tabla si lo necesitas
    }

    @FXML
    private void handleControlPortes(javafx.event.ActionEvent event) {
        System.out.println("[DEMO] Clic en Control de Portes - Funcionalidad desvinculada para la demo");
    }

    @FXML
    private void handleCataleg(javafx.event.ActionEvent event) {
        System.out.println("[DEMO] Clic en Cataleg - Funcionalidad desvinculada para la demo");
    }
}
