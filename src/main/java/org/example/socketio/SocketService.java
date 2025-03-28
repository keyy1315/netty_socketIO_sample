package org.example.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

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
            String sessionId = client.getSessionId().toString();

            log.info("Connected: {}", sessionId);
        });

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private int count = 0;
            @Override
            public void run() {
                if (count >= 10) {  // 10번 실행되면 종료
                    timer.cancel(); // 타이머 종료
                    return;
                }

                String time = LocalDateTime.now().toString();
                Notification noti = new Notification("send notification!", time);
                socketServer.getBroadcastOperations().sendEvent("notification", noti);

                count++;
            }
        };
        timer.scheduleAtFixedRate(task, 0, 3000);
    }

    @PreDestroy
    private void stopSocketServer() {
        socketServer.stop();
    }
}
