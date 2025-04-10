package scdy.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "fcm_token",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_token", columnNames = {"user_id", "fcm_token"})
        }
        )
public class FcmToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long fcmTokenId;

        private String fcmToken;

        private Long userId;

        private String deviceInfo;

        @CreatedDate
        private LocalDateTime createdAt;

        private boolean isActive;

        public void updateFcmToken(String fcmToken, String deviceInfo, boolean isActive){
                this.fcmToken = fcmToken;
                this.deviceInfo = deviceInfo;
                this.isActive = isActive;
        }

        @Builder
        public FcmToken(Long fcmTokenId, String fcmToken, Long userId, String deviceInfo, LocalDateTime createdAt, boolean isActive) {
                this.fcmTokenId = fcmTokenId;
                this.fcmToken = fcmToken;
                this.userId = userId;
                this.deviceInfo = deviceInfo;
                this.createdAt = createdAt;
                this.isActive = isActive;
        }
}
