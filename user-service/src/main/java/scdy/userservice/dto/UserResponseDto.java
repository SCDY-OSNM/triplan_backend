package scdy.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import scdy.userservice.enums.UserRole;
import scdy.userservice.entity.User;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;

    private String email;

    private String nickname;

    private UserRole userRole;

    private String phoneNumber;



    // password는 JSON 응답에서 제외되므로 제거할 수 있음
    @JsonIgnore
    private String password;

    public UserResponseDto(Long userId,
                           String email,
                           String nickname,
                           UserRole userRole,
                           String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
        this.phoneNumber = phoneNumber;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserRole(),
                user.getPhoneNumber()
        );
    }
}
