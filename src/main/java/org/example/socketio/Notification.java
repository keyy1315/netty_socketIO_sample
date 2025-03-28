package org.example.socketio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class Notification {
    private String message;
    private String time;
}
