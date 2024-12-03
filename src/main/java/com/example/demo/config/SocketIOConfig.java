package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * Configuration for Socket IO
 */
@Configuration
public class SocketIOConfig {

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private Integer port;

    @Value("${socket-server.origin}")
    private String origin;
    /**
     * Returns a Socket IO Server
     */
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host); 
        config.setPort(port); 

        config.setOrigin("*");

        return new SocketIOServer(config);
    }

}