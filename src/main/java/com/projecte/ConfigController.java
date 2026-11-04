package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfigController {
    
    @FXML private ComboBox<String> boxUbicacio;
    @FXML private TextField tfURL;
    @FXML private Label lbMissatge;

    @FXML 
    private void initialize() {
        boxUbicacio.getItems().addAll("Taquilla", "Porta", "Oficina");
    }

    @FXML
    private void onConnectar() {
        String ubicacio = boxUbicacio.getValue();
        String URL = tfURL.getText().trim();

        if (ubicacio == null) {
            mostrarError("Has de triar una ubicacio");
            return;
        }
        if (URL.isEmpty()) {
            mostrarError("La URL no es valida");
            return;
        }

        lbMissatge.setText("Connectant...");
        if (!ClientWs.connectar(URL)) {
            mostrarError("No s'ha pogut connectar al servidor");
            return;
        }

        if (!ConfigManager.desar(ubicacio, URL)) {
            mostrarError("Connectat, pero no s'ha pogut desar la configuració");
            return;
        }

        lbMissatge.setText("Connexio correcta!");

        try {
            Stage stage = (Stage) lbMissatge.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            mostrarError("No s'ha pogut obrir la pantalla de vendes");
        }
        
    }

    public void mostrarError(String text) {
        lbMissatge.setText(text);
    }
}
