package scdy.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.notificationservice.common.advice.ApiResponse;
import scdy.notificationservice.dto.TokenRequestDto;
import scdy.notificationservice.service.FcmTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fcm")
public class FcmController {
    private final FcmTokenService fcmTokenService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> updateFcmToken(@RequestHeader("X-Authenticated-User") Long userId, @RequestBody TokenRequestDto tokenRequestDto){
        try{
            fcmTokenService.createFcmToken(userId, tokenRequestDto);
            return ResponseEntity.ok(ApiResponse.success("토큰 업데이트 완료"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("토큰 업데이트 실패"));
        }
    }

    @DeleteMapping("/{fcmToken}")
    public ResponseEntity<ApiResponse<String>> deleteFcmToken(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable String fcmToken){
        fcmTokenService.deleteFcmToken(userId);
        return ResponseEntity.ok(ApiResponse.success("토큰 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<String>> getFcmToken(@RequestHeader ("X-Authenticated-User")Long userId) {
        try {
            String existToken = fcmTokenService.getFcmTokenByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success("토큰 조회 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("토큰 조회 실패"));
        }
    }
}
