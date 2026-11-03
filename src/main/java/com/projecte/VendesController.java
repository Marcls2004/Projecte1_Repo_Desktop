package com.projecte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

    @FXML private MenuButton menuEspectacle;

    private ObservableList<Venda> llistaVendes = FXCollections.observableArrayList();
    // Lista especial de JavaFX que permite aplicar filtros en tiempo real
    private FilteredList<Venda> llistaFiltrada;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataHoraProperty());
        colEspectacle.setCellValueFactory(cellData -> cellData.getValue().espectacleProperty());
        colImport.setCellValueFactory(cellData -> cellData.getValue().importVendaProperty());


        // 1. Cargamos todos los datos desde el archivo XML
        cargarDatosDesdeXML("data/tests/acontecimientos.xml");

        // 2. Inicializamos la lista filtrada envolviendo a la lista original
        llistaFiltrada = new FilteredList<>(llistaVendes, p -> true);

        // 3. Asignamos la lista con capacidades de filtro a la vista de la tabla
        tablaVendes.setItems(llistaFiltrada);
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
                
                // Creamos la opción por defecto para restaurar todos los registros
                MenuItem itemTots = new MenuItem("Espectacle: Tots");
                itemTots.setOnAction(e -> aplicarFiltro("Tots"));
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

                llistaVendes.add(new Venda(idVenda, dataHora, nom, preu));

                // Añadimos el espectáculo al desplegable y le programamos su evento de clic
                if (menuEspectacle != null) {
                    MenuItem itemEspectacle = new MenuItem(nom);
                    itemEspectacle.setOnAction(e -> aplicarFiltro(nom));
                    menuEspectacle.getItems().add(itemEspectacle);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método encargado de realizar el filtrado de filas y actualizar la interfaz gráfica
    private void aplicarFiltro(String nomEspectacle) {
        if (nomEspectacle.equals("Tots")) {
            // Elimina cualquier restricción y muestra todo
            llistaFiltrada.setPredicate(venda -> true);
            menuEspectacle.setText("Espectacle: Tots");
        } else {
            // Compara el nombre del espectáculo de cada fila con el seleccionado
            llistaFiltrada.setPredicate(venda -> venda.espectacleProperty().get().equals(nomEspectacle));
            menuEspectacle.setText("Espectacle: " + nomEspectacle);
        }

    }

}
