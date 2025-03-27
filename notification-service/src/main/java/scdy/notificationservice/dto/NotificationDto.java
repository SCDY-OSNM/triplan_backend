package scdy.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.notificationservice.entity.Notification;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationDto {

    private Long userId;

    private String noticeContents;

    private LocalDateTime noticedAt; // 알림 일시

    private Boolean isChecked;

    @Builder
    public NotificationDto(Long userId, String noticeContents, LocalDateTime noticedAt, Boolean isChecked) {
        this.userId = userId;
        this.noticeContents = noticeContents;
        this.noticedAt = noticedAt;
        this.isChecked = isChecked;
    }

    public static NotificationDto from(Notification notification){
        return NotificationDto.builder()
                .userId(notification.getUserId())
                .noticeContents(notification.getNoticeContents())
                .noticedAt(notification.getNoticedAt())
                .isChecked(notification.getIsChecked())
                .build();
    }

}
