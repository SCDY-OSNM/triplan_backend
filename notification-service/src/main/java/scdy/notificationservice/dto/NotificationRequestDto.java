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

    private String noticeTitle;

    private String noticeContents;

    private LocalDateTime noticedAt; // 알림 일시

    private Boolean isChecked;

    @Builder
    public NotificationRequestDto(Long userId, String noticeTitle, String noticeContents, LocalDateTime noticedAt, Boolean isChecked) {
        this.userId = userId;
        this.noticeTitle = noticeTitle;
        this.noticeContents = noticeContents;
        this.noticedAt = noticedAt;
        this.isChecked = isChecked;
    }

    public static NotificationRequestDto from(Notification notification){
        return NotificationRequestDto.builder()
                .userId(notification.getUserId())
                .noticeTitle(notification.getNoticeTitle())
                .noticeContents(notification.getNoticeContents())
                .noticedAt(notification.getNoticedAt())
                .isChecked(notification.getIsChecked())
                .build();
    }

}
