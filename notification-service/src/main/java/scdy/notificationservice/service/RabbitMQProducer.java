package scdy.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.notificationservice.dto.NotificationRequestDto;
import scdy.notificationservice.entity.Notification;
import scdy.notificationservice.repository.NotificationRepository;

@Slf4j
@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;


    public RabbitMQProducer(RabbitTemplate rabbitTemplate, NotificationRepository notificationRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public NotificationRequestDto sendNotification(NotificationRequestDto notificationDto) {
        Notification notification = Notification.builder()
                .userId(notificationDto.getUserId())
                .noticeContents(notificationDto.getNoticeContents())
                .noticedAt(notificationDto.getNoticedAt())
                .isChecked(false)
                .build();
        notificationRepository.save(notification);

        // NotificationDto를 전송하여 타입 일관성 유지
        rabbitTemplate.convertAndSend("NotificationExchange", "key", notificationDto);

        log.info("Send Notification : {}", notificationDto.toString());
        return notificationDto;
    }
}