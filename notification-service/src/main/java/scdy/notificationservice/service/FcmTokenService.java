package scdy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.notificationservice.dto.TokenRequestDto;
import scdy.notificationservice.entity.FcmToken;
import scdy.notificationservice.repository.FcmTokenRepository;
import scdy.notificationservice.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    // 토큰 생성
    @Transactional
    public void createFcmToken(Long userId, TokenRequestDto tokenRequestDto) {
        FcmToken existingToken = fcmTokenRepository.findLatestByUserIdOrElseThrow(userId);

        if (existingToken!= null) {
            fcmTokenRepository.save(existingToken);
        } else {
            FcmToken newToken = FcmToken.builder()
                    .userId(userId)
                    .fcmToken(tokenRequestDto.getFcmToken())
                    .deviceInfo(tokenRequestDto.getDeviceInfo())
                    .isActive(true)
                    .build();
            fcmTokenRepository.save(newToken);
        }
    }

    // 토큰 조회
    public String getFcmTokenByUserId(Long userId){
        return fcmTokenRepository.findLatestByUserId(userId)
                .map(FcmToken::getFcmToken)
                .orElseThrow(() -> new IllegalArgumentException("FCM Token not found for userId: " + userId));
    }

    // 토큰 삭제
    @Transactional
    public void deleteFcmToken(Long userId){
        FcmToken fcmToken = fcmTokenRepository.findLatestByUserIdOrElseThrow(userId);
        fcmTokenRepository.deleteById(fcmToken.getFcmTokenId());
    }


}
