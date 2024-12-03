package com.example.demo.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * Log for connections on the Socket IO server
 */
@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    /**
     * Runs the Socket IO server
     * @param server 
     */
    @Autowired
    public ServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    /**
     * Main function
     */
    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("Servidor Socket.IO iniciado en el puerto " + server.getConfiguration().getPort());
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                String room = client.getHandshakeData().getSingleUrlParam("room");
                client.joinRoom(room);
                System.out.println("Cliente conectado: " + client.getSessionId() + ", sala: " + room);
            }
        });

        server.addEventListener("enviar_listo", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String room = (String) data.get("nombreSala");
                String nombreUsuario = (String) data.get("nombreUsuario");
                Boolean ready = (Boolean) data.get("ready");
                System.out.println("Recibido 'enviar_listo' de " + nombreUsuario + " en la sala " + room);
                data.put("ready", ready);
                server.getRoomOperations(room).sendEvent("estado_actualizado", data);
                System.out.println("Transmitiendo 'estado_actualizado' a la sala " + room + " con datos: " + data);
            }
        });

        server.addEventListener("presionar_tecla", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String room = client.getHandshakeData().getSingleUrlParam("room");
                String playerId = (String) data.get("playerId");
                String direction = (String) data.get("direction");
                System.out.println("Recibida tecla '" + direction + "' para el jugador " + playerId);
                server.getRoomOperations(room).sendEvent("tecla_presionada", data);
                System.out.println("Transmitiendo 'tecla_presionada' a la sala " + room + " con datos: " + data);
            }
        });

        
        server.addEventListener("cambiar_color", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String room = client.getHandshakeData().getSingleUrlParam("room");
                server.getRoomOperations(room).sendEvent("color_presionado", data);
                System.out.println("Transmitiendo 'color_presionada' a la sala " + room + " con datos: " + data);
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("Cliente desconectado: " + client.getSessionId());
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Deteniendo el servidor Socket.IO...");
            server.stop();
        }));
    }
}
