package scdy.notificationservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import scdy.notificationservice.dto.NotificationRequestDto;


@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final FcmService fcmService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void pushConsumer(NotificationRequestDto notificationDto) {
        log.info("Received Notification : {}", notificationDto.toString());
        String response = fcmService.sendNotification(notificationDto.getUserId(), notificationDto.getNoticeTitle(), notificationDto.getNoticeContents());
        log.info(response);

    }
}

