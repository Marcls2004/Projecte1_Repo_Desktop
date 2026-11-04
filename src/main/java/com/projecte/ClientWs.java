package com.projecte;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ClientWs extends WebSocketClient {
    private static ClientWs connexio;

    public ClientWs(URI uri) {
        super(uri);
    }

    public static boolean connectar(String url) {
        try {
            // si ya habia una conexion anterior la cerramos
            tancar();

            ClientWs client = new ClientWs(new URI(url));
            
            // Esperar un maximo de 3 segundos
            if (!client.connectBlocking(3, TimeUnit.SECONDS)) {
                System.out.println("ERROR: no s'ha pogut connectar a " + url);
                return false;
            }
            connexio = client;
            return true;
        } catch (Exception e) {
            // URL mal formada, etc.
            System.out.println("ERROR: connexio fallida: " + e.getMessage());
            return false;
        }
    }

    public static void tancar() {
        if (connexio != null) {
            connexio.close();
            connexio = null;
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connectat al servidor");
    }

    @Override
    public void onMessage(String message) {
        // De momento solo mostramos lo que llega (el "welcome" del servidor)
        System.out.println("Missatge rebut: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Desconnectat (codi " + code + ")");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error de WebSocket: " + ex.getMessage());
    }
}
