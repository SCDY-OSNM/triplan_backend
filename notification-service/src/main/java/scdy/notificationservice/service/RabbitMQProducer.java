package scdy.notificationservice.service;

<<<<<<< HEAD
=======
import lombok.RequiredArgsConstructor;
>>>>>>> 708159e71076ffc1d4541690aeb6d925af928afb
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<<<<<<< HEAD
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
=======
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
>>>>>>> 708159e71076ffc1d4541690aeb6d925af928afb
                .userId(notificationDto.getUserId())
                .noticeContents(notificationDto.getNoticeContents())
                .noticedAt(notificationDto.getNoticedAt())
                .isChecked(false)
                .build();
        notificationRepository.save(notification);

<<<<<<< HEAD
        // NotificationDto를 전송하여 타입 일관성 유지
        rabbitTemplate.convertAndSend("NotificationExchange", "key", notificationDto);

        log.info("Send Notification : {}", notificationDto.toString());
        return notificationDto;
    }
}
=======
        rabbitTemplate.convertAndSend("notificationQueue", notification);

        log.info("Send Notification : {}", notificationDto.toString());
        return NotificationDto.from(notification);
    }
}
>>>>>>> 708159e71076ffc1d4541690aeb6d925af928afb
