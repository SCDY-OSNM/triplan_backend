package scdy.planservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import scdy.planservice.common.advice.ApiResponse;
import scdy.planservice.dto.UserResponseDto;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserResponseDto> getUserById(@PathVariable("userId")Long userId);
}
