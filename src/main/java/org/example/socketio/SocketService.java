package org.example.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketService {

    @Autowired
    private SocketIOServer socketServer;

    @PostConstruct
    private void initSocketEvents() {
        socketServer.addEventListener("message", Object.class, (client, data, ackRequest) -> {
            log.info("Received message: {}", data);
            socketServer.getBroadcastOperations().sendEvent("message", data);
        });

        socketServer.addEventListener("join", Object.class, (client, username, ackRequest) -> {
            String name = username instanceof String ? (String) username : username.toString();

            log.info("User joined: {}", name);

            // Create join message
            Message joinMessage = Message.builder()
                    .message(name + " joined the chat")
                    .sender("System")
                    .type(Message.MessageType.JOIN)
                    .build();

            socketServer.getBroadcastOperations().sendEvent("message", joinMessage);
        });
    }

    @PreDestroy
    private void stopSocketServer() {
        socketServer.stop();
    }
}
