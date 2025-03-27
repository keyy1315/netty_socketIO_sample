package org.example.socketio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String message;
    private String sender;
    private MessageType type;

    public Message(String data) {
        this.message = data;
        this.type = MessageType.CHAT;
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    public MessageType getType() {
        return type != null ? type : MessageType.CHAT;
    }
}
