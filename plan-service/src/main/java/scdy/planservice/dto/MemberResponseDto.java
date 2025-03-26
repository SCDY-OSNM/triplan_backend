package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.MemberRole;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long planId;

    private Long userId;

    private MemberRole memberRole;

    @Builder
    public MemberResponseDto(Long planId, Long userId, MemberRole memberRole) {
        this.planId = planId;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .planId(member.getPlanId())
                .userId(member.getUserId())
                .memberRole(member.getMemberRole())
                .build();
    }
}
