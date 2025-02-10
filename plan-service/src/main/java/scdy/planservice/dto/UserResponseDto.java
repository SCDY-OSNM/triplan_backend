package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;

    private String email;

    @Builder
    public UserResponseDto(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
    // userClient에서 user를 받아올 때 꼭 user-service에 있는 userResponseDto에 있는 모든 정보가 담겨있어야하나요?
    // id랑 email만 받으면 오류가 날까요
}
