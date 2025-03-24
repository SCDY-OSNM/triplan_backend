package scdy.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    private String fcmToken;
    private String deviceInfo;

    @Builder
    public TokenRequestDto(String fcmToken, String deviceInfo) {
        this.fcmToken = fcmToken;
        this.deviceInfo = deviceInfo;
    }
}
