package com.projecte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;

import org.w3c.dom.Document;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Por defecto pantalla de configuracion
        String pantalla = "/assets/pantalla_inici.fxml";

        // Si ya hay configuración guardada, intentamos reconectar
        if (ConfigManager.existeix()) {
            Document doc = ConfigManager.carregar();

            if (doc != null) {
                String url = doc.getDocumentElement().getAttribute("url");

                // Si reconecta bien, nos saltamos la configuración
                if (ClientWs.connectar(url)) {
                    pantalla = "/assets/llistat_vendes.fxml";
                }
                // Si falla, nos quedamos en pantalla_inici para corregir
            }
        }

        // Carga de la interfaz gráfica
        Parent root = FXMLLoader.load(getClass().getResource(pantalla));
        primaryStage.setTitle("Entradium - Sistema de Gestió d'entrades");
        
        // Bloque de seguridad para evitar que el programa se caiga si la foto no está
        try {
            InputStream iconStream = getClass().getResourceAsStream("/icons/icon.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            } else {
                System.out.println("[Avis] No s'ha trobat la imatge a 'src/main/resources/icons/icon.png'. Es farà servir l'icona per defecte.");
            }
        } catch (Exception e) {
            System.out.println("[Error] No s'ha pogut carregar l'icona de la finestra: " + e.getMessage());
        }

        primaryStage.setScene(new Scene(root, 780, 520));
        primaryStage.show();
    }

    // JavaFX lo llama al cerrar la ventana
    @Override
    public void stop() {
        ClientWs.tancar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
