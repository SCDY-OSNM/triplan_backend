package scdy.notificationservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scdy.notificationservice.entity.FcmToken;
import scdy.notificationservice.repository.FcmTokenRepository;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    // 알림 전송
    public String sendNotification(Long userId, String title, String body) {
        try {
            String fcmToken = getFcmTokenByUserId(userId);

            if (fcmToken != null) {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .build();

                String response = firebaseMessaging.send(message);
                return "알림 전송 성공" + response;
            } else {
                System.out.println("토큰이 발급되지 않은 유저" + userId);
                return "토큰이 발급되지 않은 유저" + userId;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "알림 전송 실패: " + e.getMessage();
        }
    }

    public String getFcmTokenByUserId(Long userId) {
        return fcmTokenRepository.findLatestByUserId(userId)
                .map(FcmToken::getFcmToken)
                .orElseThrow(() -> new IllegalArgumentException("FCM Token not found for userId: " + userId));
    }

}
