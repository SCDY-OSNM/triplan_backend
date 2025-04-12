package scdy.planservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDto {
    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    private MessageType messageType;

    private String message;

    private Long messageSenderId;

    private String roomId;

    @Builder

    public ChatMessageDto(MessageType messageType, String message, Long messageSenderId, String roomId) {
        this.messageType = messageType;
        this.message = message;
        this.messageSenderId = messageSenderId;
        this.roomId = roomId;
    }
}
