package scdy.reservationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private Boolean isDeleted;
    private String userRole;
    private Long kakaoId;
}
