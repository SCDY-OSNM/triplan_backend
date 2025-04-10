package scdy.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.notificationservice.entity.Notification;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationMessage {

    private String noticeTitle;

    private String noticeBody;

    private Long userId;

    private LocalDateTime noticedAt; // 알림 일시

    private Boolean isChecked;

    @Builder
    public NotificationMessage(String noticeTitle, String noticeBody, Long userId, LocalDateTime noticedAt, Boolean isChecked) {
        this.noticeTitle = noticeTitle;
        this.noticeBody = noticeBody;
        this.userId = userId;
        this.noticedAt = noticedAt;
        this.isChecked = isChecked;
    }

    public static NotificationMessage from(Notification notification){
        return NotificationMessage.builder()
                .noticeTitle(notification.getNoticeTitle())
                .noticeBody(notification.getNoticeBody())
                .userId(notification.getUserId())
                .noticedAt(notification.getNoticedAt())
                .isChecked(notification.getIsChecked())
                .build();
    }

}
