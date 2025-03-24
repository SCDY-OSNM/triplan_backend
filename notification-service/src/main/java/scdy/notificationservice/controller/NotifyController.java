package scdy.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import scdy.notificationservice.service.RabbitMQConsumer;
import scdy.notificationservice.service.RabbitMQProducer;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/notifications")
public class NotifyController {
    private final RabbitMQConsumer rabbitMQConsumer;

    private final RabbitMQProducer rabbitMQProducer;

    /* 송신 테스트 코드
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(@RequestBody NotificationDto notificationDto){
        rabbitMQProducer.sendNotification(notificationDto);
        return ResponseEntity.ok(ApiResponse.success("알림 송신 완료", notificationDto));
    } */

}
