package scdy.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId")
    private Long notificationId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String noticeContents;

    @Column(nullable = false)
    private LocalDateTime noticedAt;

    @Column(nullable = false)
    private Boolean isChecked;

    @Builder
    public Notification(Long userId, String noticeContents, LocalDateTime noticedAt, Boolean isChecked){
        this.userId = userId;
        this.noticeContents = noticeContents;
        this.noticedAt = noticedAt;
        this.isChecked = isChecked;
    }

    public void updateIsChecked(Boolean isChecked){
        this.isChecked = isChecked;
    }

}
