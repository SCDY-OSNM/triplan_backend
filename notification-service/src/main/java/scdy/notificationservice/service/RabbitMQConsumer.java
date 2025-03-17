package scdy.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import scdy.notificationservice.dto.NotificationDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void pushConsumer(NotificationDto notificationDto){
        log.info("Received Nofitication : {}", notificationDto.toString());
    } // queue에서 메세지를 구독

}
