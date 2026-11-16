package com.projecte;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserController {

    @FXML private ComboBox<String> boxUbicacio;
    @FXML private TextField tfURL;
    @FXML private Label lbMissatge;
    @FXML private Button btTornar;

    @FXML
    public void initialize() {
        boxUbicacio.getItems().addAll("Oficina", "Taquilla", "Porta");

        if (lbMissatge != null) {
            lbMissatge.setWrapText(true);
        }

        // Cargamos los valores que ya existen en data/CONFIG.xml al abrir la pantalla
        Document doc = ConfigManager.carregar();
        if (doc != null) {
            Element arrel = doc.getDocumentElement();
            String ubicacio = arrel.getAttribute("ubicacio");
            String url = arrel.getAttribute("url");

            if (ubicacio != null && !ubicacio.isEmpty()) {
                boxUbicacio.setValue(ubicacio);
            }
            if (url != null && !url.isEmpty()) {
                tfURL.setText(url);
            }
        }
    }

    @FXML
    private void onDesar() {
        String ubicacio = boxUbicacio.getValue();
        String URL = tfURL.getText().trim();

        // 1. Validamos que haya una ubicación elegida
        if (ubicacio == null || ubicacio.isEmpty()) {
            mostrarError("Has de triar una ubicacio");
            return;
        }

        // 2. Validamos que la URL no esté vacía y tenga formato ws:// o wss:// válido
        if (URL.isEmpty() || (!URL.startsWith("ws://") && !URL.startsWith("wss://")) || URL.length() <= 6) {
            mostrarError("La URL no es valida (ha de començar per ws:// o wss://)");
            return;
        }

        // 3. Probamos a conectar primero antes de guardar nada en CONFIG.xml
        lbMissatge.setStyle("-fx-text-fill: black;");
        lbMissatge.setText("Connectant...");

        if (!ClientWs.connectar(URL)) {
            mostrarError("No s'ha pogut connectar al servidor");
            return;
        }

        // 4. Solo si la conexión funciona, guardamos en data/CONFIG.xml
        if (!ConfigManager.desar(ubicacio, URL)) {
            mostrarError("Connectat, pero no s'ha pogut desar la configuració");
            return;
        }

        lbMissatge.setStyle("-fx-text-fill: green;");
        lbMissatge.setText("Connexio correcta!");

        // 5. Volvemos al listado de ventas
        try {
            Stage stage = (Stage) lbMissatge.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            mostrarError("No s'ha pogut obrir la pantalla de vendes");
        }
    }

    @FXML
    private void onTornar(ActionEvent event) {
        try {
            Node boto = (Node) event.getSource();
            Stage stage = (Stage) boto.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            mostrarError("No s'ha pogut tornar al llistat de vendes");
        }
    }

    private void mostrarError(String missatge) {
        lbMissatge.setStyle("-fx-text-fill: red;");
        lbMissatge.setText(missatge);
    }
}
