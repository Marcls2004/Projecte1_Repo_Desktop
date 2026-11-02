package com.projecte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class VendesController {

    @FXML private TableView<Venda> tablaVendes;
    @FXML private TableColumn<Venda, String> colId;
    @FXML private TableColumn<Venda, String> colData;
    @FXML private TableColumn<Venda, String> colEspectacle;
    @FXML private TableColumn<Venda, String> colImport;
    @FXML private TableColumn<Venda, String> colEstat;
    @FXML private TableColumn<Venda, String> colAccions;

    @FXML private MenuButton menuEspectacle;
    @FXML private Label lblRegistres;

    private ObservableList<Venda> llistaVendes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataHoraProperty());
        colEspectacle.setCellValueFactory(cellData -> cellData.getValue().espectacleProperty());
        colImport.setCellValueFactory(cellData -> cellData.getValue().importVendaProperty());
        colEstat.setCellValueFactory(cellData -> cellData.getValue().estatProperty());
        colAccions.setCellValueFactory(cellData -> cellData.getValue().accionsProperty());

        cargarDatosDesdeXML("data/tests/acontecimientos.xml");

        tablaVendes.setItems(llistaVendes);
    }

    private void cargarDatosDesdeXML(String rutaArchivo) {
        try {
            File xmlFile = new File(rutaArchivo);
            if (!xmlFile.exists()) {
                System.err.println("Error: No se encuentra el archivo XML en: " + xmlFile.getAbsolutePath());
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            if (menuEspectacle != null) {
                menuEspectacle.getItems().clear();
                MenuItem itemTots = new MenuItem("Espectacle: Tots");
                menuEspectacle.getItems().add(itemTots);
            }

            NodeList esdeveniments = doc.getElementsByTagName("esdeveniment");
            int totalRegistres = esdeveniments.getLength();

            for (int i = 0; i < totalRegistres; i++) {
                Element esdeveniment = (Element) esdeveniments.item(i);
                
                String idVenda = "#V-8" + (8991 - i); 
                String nom = esdeveniment.getElementsByTagName("nom").item(0).getTextContent();
                String dataHora = esdeveniment.getElementsByTagName("dataHora").item(0).getTextContent().replace("T", " ");
                
                NodeList tipusEntrades = esdeveniment.getElementsByTagName("tipusEntrada");
                String preu = "0.00€";
                if (tipusEntrades.getLength() > 0) {
                    Element primeraEntrada = (Element) tipusEntrades.item(0);
                    preu = primeraEntrada.getAttribute("preu") + "€";
                }

                String estat = (i % 3 == 2) ? "Reemb." : "Pagat";

                llistaVendes.add(new Venda(idVenda, dataHora, nom, preu, estat));

                if (menuEspectacle != null) {
                    MenuItem itemEspectacle = new MenuItem(nom);
                    menuEspectacle.getItems().add(itemEspectacle);
                }
            }

            if (lblRegistres != null) {
                lblRegistres.setText("Mostrant 1-" + totalRegistres + " de " + totalRegistres + " registers");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
