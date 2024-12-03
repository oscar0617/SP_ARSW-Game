package com.example.demo;

import com.corundumstudio.socketio.SocketIOServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;

import static org.junit.Assert.*;

public class DemoApplicationTests {

    private SocketIOServer server;
    private Socket client;

    @Before
    public void setUp() throws URISyntaxException {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        config.setOrigin("*");

        server = new SocketIOServer(config);
        server.start();
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;

        client = IO.socket("http://localhost:9092", options);
        client.connect();
    }

    @After
    public void tearDown() {
        if (client != null && client.connected()) {
            client.disconnect();
        }
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testEventEnviarListo() {
        server.addEventListener("enviar_listo", Map.class, (client, data, ackRequest) -> {
            String room = (String) data.get("nombreSala");
            String nombreUsuario = (String) data.get("nombreUsuario");
            Boolean ready = (Boolean) data.get("ready");
            assertEquals("testRoom", room);
            assertEquals("testUser", nombreUsuario);
            assertTrue(ready);
            Map<String, Object> response = new HashMap<>();
            response.put("estado", "ok");
            client.sendEvent("estado_actualizado", response);
        });
        client.on("estado_actualizado", args -> {
            Map<String, Object> response = (Map<String, Object>) args[0];
            assertEquals("ok", response.get("estado"));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("nombreSala", "testRoom");
        data.put("nombreUsuario", "testUser");
        data.put("ready", true);

        client.emit("enviar_listo", data);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEventPresionarTecla() {
        server.addEventListener("presionar_tecla", Map.class, (client, data, ackRequest) -> {
            String direction = (String) data.get("direction");
            assertEquals("up", direction);
            client.sendEvent("tecla_presionada", data);
        });
        client.on("tecla_presionada", args -> {
            Map<String, Object> response = (Map<String, Object>) args[0];
            assertEquals("up", response.get("direction"));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("direction", "up");

        client.emit("presionar_tecla", data);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
