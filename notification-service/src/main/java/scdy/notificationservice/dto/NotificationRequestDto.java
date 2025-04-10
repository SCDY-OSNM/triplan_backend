package scdy.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.notificationservice.entity.Notification;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationRequestDto {

    private Long userId;

    private NotificationMessage notificationMessage;

    @Builder
    public NotificationRequestDto(Long userId, NotificationMessage notificationMessage) {
        this.userId = userId;
        this.notificationMessage = notificationMessage;
    }
}
