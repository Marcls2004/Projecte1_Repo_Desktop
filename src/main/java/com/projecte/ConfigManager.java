package com.projecte;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ConfigManager {
    private static final File FITXER = new  File("data/CONFIG.xml");

    public static boolean existeix() {
        return FITXER.exists();
    }

    // Guarda ubicación y URL en CONFIG.XML.
    // Devuelve true si todo ha ido bien y false si ha fallado algo,
    // igual que parseXML del servidor devuelve null cuando falla
    public static boolean desar(String ubicacio, String url) {
        try {
            // Creamos un Document vacío en memoria.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz: <configuracio>
            Element arrel = doc.createElement("configuracio");

            // Guardamos los datos como atributos, no como elementos hijos,
            // igual que en el catálogo (<tipusEntrada preu="..." quota="...">)
            arrel.setAttribute("ubicacio", ubicacio);
            arrel.setAttribute("url", url);

            // Crear el elemento no basta: hay que colgarlo del documento,
            // si no, no aparecerá en el fichero
            doc.appendChild(arrel);

            // El Transformer convierte el árbol en memoria en texto
            // y lo escribe en el fichero
            Transformer t = TransformerFactory.newInstance().newTransformer();

            // Sin esto el XML saldría todo en una sola línea
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            // Origen: el Document / Destino: el fichero CONFIG.XML
            t.transform(new DOMSource(doc), new StreamResult(FITXER));

            return true;

        } catch (Exception e) {
            // No propagamos la excepción: avisamos por consola y devolvemos
            // false para que el controlador decida qué mostrar al usuario
            System.out.println("ERROR: no s'ha pogut desar la configuracio");
            return false;
        }
    }

    public static Document carregar() {
        if (!existeix()) {
            return null;
        }
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(FITXER);
            return doc;
        } catch (Exception e) {
            System.out.println("ERROR: no s'ha pogut obtenir el fitxer de configuracio");
            return null;
        }
    }
}
