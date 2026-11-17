package com.projecte;

import org.json.JSONObject;
import org.w3c.dom.Document;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PortesController {

    @FXML private TextField tfLocalitzador;
    @FXML private VBox panellResultat;
    @FXML private Label lbResultat;
    @FXML private Label lbDetall;
    @FXML private Button btnTornar;

    @FXML
    private void initialize() {
        ClientWs.establirReceptor(this::rebreMissatge);

        // En mode Porta només es mostra el validador: no hi ha botó per tornar enrere
        Document doc = ConfigManager.carregar();
        if (doc != null && "Porta".equals(doc.getDocumentElement().getAttribute("ubicacio"))) {
            btnTornar.setVisible(false);
            btnTornar.setManaged(false);
        }

        Platform.runLater(() -> tfLocalitzador.requestFocus());
    }

    @FXML
    private void validar() {
        String localitzador = tfLocalitzador.getText().trim();

        if (localitzador.isEmpty()) {
            mostrarResultat(false, "ENTRADA NO VÀLIDA", "Escriu un localitzador");
            return;
        }

        JSONObject peticio = new JSONObject();
        peticio.put("type", "validarEntrada");
        peticio.put("localitzador", localitzador);
        ClientWs.enviar(peticio.toString());
    }

    private void rebreMissatge(String message) {
        try {
            JSONObject resposta = new JSONObject(message);
            String type = resposta.optString("type");

            if ("validacio".equals(type)) {
                Platform.runLater(() -> {
                    if (resposta.optBoolean("ok")) {
                        String seient = resposta.optString("seient");
                        if (seient.isEmpty()) seient = "Entrada general (sense seient)";

                        mostrarResultat(true, "ENTRADA VÀLIDA",
                                "Titular: " + resposta.optString("titular") + "\n"
                                + "Tipus d'entrada: " + resposta.optString("tipus") + "\n"
                                + "Seient: " + seient);
                    } else {
                        mostrarResultat(false, "ENTRADA REBUTJADA", resposta.optString("missatge"));
                    }
                    tfLocalitzador.clear();
                    tfLocalitzador.requestFocus();
                });
            } else if ("error".equals(type)) {
                Platform.runLater(() ->
                        mostrarResultat(false, "ERROR", resposta.optString("message")));
            }
        } catch (Exception e) {
            System.out.println("Error processant la validació: " + e.getMessage());
        }
    }

    // Verd si és vàlida, vermell si es rebutja
    private void mostrarResultat(boolean valida, String titol, String detall) {
        String color = valida ? "#27ae60" : "#c0392b";
        panellResultat.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        lbResultat.setText(titol);
        lbResultat.setStyle("-fx-text-fill: white;");
        lbDetall.setText(detall);
    }

    @FXML
    private void tornar(ActionEvent event) {
        try {
            Node boto = (Node) event.getSource();
            Stage stage = (Stage) boto.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println("No s'ha pogut tornar al llistat: " + e.getMessage());
        }
    }
}