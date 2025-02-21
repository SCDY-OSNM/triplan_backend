package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.MemberRole;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private Plan plan;

    private Long userId;

    private MemberRole memberRole;

    @Builder
    public MemberRequestDto(Plan plan, Long userId, MemberRole memberRole) {
        this.plan = plan;
        this.userId = userId;
        this.memberRole = memberRole;
    }
}
