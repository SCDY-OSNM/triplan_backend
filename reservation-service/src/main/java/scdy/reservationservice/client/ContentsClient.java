package scdy.reservationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import scdy.reservationservice.common.advice.ApiResponse;
import scdy.reservationservice.dto.ContentsResponseDto;

@FeignClient(name = "contents-service")
public interface ContentsClient {

    @GetMapping("/api/v1/contents/{contentsId}")
    ApiResponse<ContentsResponseDto> getContentsById(@PathVariable("contentsId") Long contentsId);
}
