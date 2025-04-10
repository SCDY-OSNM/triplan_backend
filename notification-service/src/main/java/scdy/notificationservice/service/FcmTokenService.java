package scdy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.notificationservice.common.exceptions.NotFoundException;
import scdy.notificationservice.dto.TokenRequestDto;
import scdy.notificationservice.entity.FcmToken;
import scdy.notificationservice.repository.FcmTokenRepository;
import scdy.notificationservice.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    // 토큰 생성
    @Transactional
    public void createFcmToken(Long userId, TokenRequestDto tokenRequestDto) {
        try {
            Optional<FcmToken> optionalToken = fcmTokenRepository.findLatestByUserId(userId);

            if (optionalToken.isPresent()) {
                FcmToken existingToken = optionalToken.get();
                existingToken.updateFcmToken(tokenRequestDto.getFcmToken(), tokenRequestDto.getDeviceInfo(), true);
                fcmTokenRepository.save(existingToken);
            } else {
                FcmToken newToken = FcmToken.builder()
                        .userId(userId)
                        .createdAt(LocalDateTime.now())
                        .fcmToken(tokenRequestDto.getFcmToken())
                        .deviceInfo(tokenRequestDto.getDeviceInfo())
                        .isActive(true)
                        .build();
                fcmTokenRepository.save(newToken);
            }
        } catch (Exception e) {
            log.error("FCM 토큰 저장 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("FCM 토큰 저장 실패: " + e.getMessage(), e);
        }
    }

    // 토큰 조회
    public String getFcmTokenByUserId(Long userId){
        return fcmTokenRepository.findLatestByUserId(userId)
                .map(FcmToken::getFcmToken)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 FCM 토큰이 존재하지 않습니다 "));
    }

    // 토큰 삭제
    @Transactional
    public void deleteFcmToken(Long fcmTokenId){
        fcmTokenRepository.findByIdOrElseThrow(fcmTokenId);
        fcmTokenRepository.deleteById(fcmTokenId);
    }


}
