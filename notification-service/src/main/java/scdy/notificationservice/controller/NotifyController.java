package scdy.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.notificationservice.common.advice.ApiResponse;
import scdy.notificationservice.dto.NotificationDto;
import scdy.notificationservice.service.RabbitMQConsumer;
import scdy.notificationservice.service.RabbitMQProducer;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/notice")
public class NotifyController {
    private final RabbitMQConsumer rabbitMQConsumer;

    private final RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(@RequestBody NotificationDto notificationDto){
        rabbitMQProducer.sendNotification(notificationDto);
        return ResponseEntity.ok(ApiResponse.success("알림 수신 완료", notificationDto));
    }

}
