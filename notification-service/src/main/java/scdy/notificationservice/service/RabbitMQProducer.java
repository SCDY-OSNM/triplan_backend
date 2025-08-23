package scdy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.notificationservice.dto.NotificationDto;
import scdy.notificationservice.entity.Notification;
import scdy.notificationservice.repository.NotificationRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RabbitMQProducer {
    public final RabbitTemplate rabbitTemplate;

    private NotificationRepository notificationRepository;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public NotificationDto sendNotification(NotificationDto notificationDto){
        Notification notification =  Notification.builder()
                .userId(notificationDto.getUserId())
                .noticeContents(notificationDto.getNoticeContents())
                .noticedAt(notificationDto.getNoticedAt())
                .isChecked(false)
                .build();
        notificationRepository.save(notification);

        rabbitTemplate.convertAndSend("notificationQueue", notification);

        log.info("Send Notification : {}", notificationDto.toString());
        return NotificationDto.from(notification);
    }
}
