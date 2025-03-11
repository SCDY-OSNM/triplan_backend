package scdy.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import scdy.notificationservice.common.advice.ApiResponse;
import scdy.notificationservice.dto.UserResponseDto;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping
    ApiResponse<UserResponseDto> getUserById(@PathVariable("userId") Long userId);
}
