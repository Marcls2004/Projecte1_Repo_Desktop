package com.projecte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga de la interfaz gráfica
        Parent root = FXMLLoader.load(getClass().getResource("/assets/llistat_vendes.fxml"));
        primaryStage.setTitle("Llistat de Vendes - Sistema de Gestió");
        
        // Bloque de seguridad para evitar que el programa se caiga si la foto no está
        try {
            InputStream iconStream = getClass().getResourceAsStream("/icons/logo.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            } else {
                System.out.println("[Avis] No s'ha trobat la imatge a 'src/main/resources/icons/logo.png'. Es farà servir l'icona per defecte.");
            }
        } catch (Exception e) {
            System.out.println("[Error] No s'ha pogut carregar l'icona de la finestra: " + e.getMessage());
        }

        primaryStage.setScene(new Scene(root, 780, 520));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
