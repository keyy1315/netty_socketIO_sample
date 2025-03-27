package org.example.socketio;


import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example.socketio")
public class SocketIOConfig {

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8081);

        config.setOrigin("*");

        server = new SocketIOServer(config);
        server.start();
        return server;
    }

    @PreDestroy
    public void startSocketTioServer() {
        if(server != null) {
            server.stop();
        }
    }
}
