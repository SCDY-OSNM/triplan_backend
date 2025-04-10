package scdy.notificationservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import scdy.notificationservice.client.UserClient;
import scdy.notificationservice.dto.NotificationMessage;
import scdy.notificationservice.dto.UserResponseDto;
import scdy.notificationservice.entity.Notification;
import scdy.notificationservice.repository.NotificationRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final FcmService fcmService;
    private final FcmTokenService fcmTokenService;
    private final NotificationRepository notificationRepository;
    private final UserClient userClient;

    // RabbitMQ에서 메세지 consume -> fcm서비스 호출 -> fcm에 알림 전송
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void pushConsumer(NotificationMessage notificationMessage) {
        try {
            Long userId = notificationMessage.getUserId();

            String fcmToken = fcmTokenService.getFcmTokenByUserId(userId);
            if (fcmToken.isEmpty()) {
                log.warn("유저 토큰이 존재하지 않음 : {}", userId);
                return;
            }

            // Fcm에 전송
            String response = fcmService.sendNotification(notificationMessage);
            log.info(response);

            // DB 저장
            Notification notification = Notification.builder()
                    .userId(notificationMessage.getUserId())
                    .noticeTitle(notificationMessage.getNoticeTitle())
                    .noticeBody(notificationMessage.getNoticeBody())
                    .noticedAt(notificationMessage.getNoticedAt())
                    .isChecked(false)
                    .build();

            notificationRepository.save(notification);
        } catch (IllegalArgumentException e) {
            log.warn("메시지 처리 중 오류 발생: {}, userId={}", e.getMessage(), notificationMessage.getUserId());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
        }
    }
}


