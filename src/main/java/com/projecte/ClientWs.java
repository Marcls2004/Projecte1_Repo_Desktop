
package com.projecte;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ClientWs extends WebSocketClient {

    private static ClientWs connexio;

    // Controlador que recibirá los mensajes del servidor
    private Consumer<String> receptorMissatges;

    public ClientWs(URI uri) {
        super(uri);
    }

    public static boolean connectar(String url) {
        try {
            tancar();

            ClientWs client = new ClientWs(new URI(url));

            if (!client.connectBlocking(3, TimeUnit.SECONDS)) {
                System.out.println("ERROR: no s'ha pogut connectar a " + url);
                return false;
            }

            connexio = client;
            return true;

        } catch (Exception e) {
            System.out.println("ERROR: connexio fallida: " + e.getMessage());
            return false;
        }
    }

    public static void enviar(String missatge) {
        if (connexio != null && connexio.isOpen()) {
            connexio.send(missatge);
        } else {
            System.out.println("ERROR: no hi ha connexio amb el servidor");
        }
    }

    public static void establirReceptor(Consumer<String> receptor) {
        if (connexio != null) {
            connexio.receptorMissatges = receptor;
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
        System.out.println("Missatge rebut: " + message);

        if (receptorMissatges != null) {
            receptorMissatges.accept(message);
        }
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
