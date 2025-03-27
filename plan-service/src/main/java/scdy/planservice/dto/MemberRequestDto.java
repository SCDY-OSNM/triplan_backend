package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.MemberRole;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private Long planId;

    private Long userId;

    private MemberRole memberRole;

    @Builder
    public MemberRequestDto(Long planId, Long userId, MemberRole memberRole) {
        this.planId = planId;
        this.userId = userId;
        this.memberRole = memberRole;
    }
}
