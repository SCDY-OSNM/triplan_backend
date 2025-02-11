package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.MemberRole;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Plan plan;

    private Long userId;

    private MemberRole memberRole;

    @Builder
    public MemberResponseDto(Plan plan, Long userId, MemberRole memberRole) {
        this.plan = plan;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .plan(member.getPlan())
                .userId(member.getUserId())
                .memberRole(member.getMemberRole())
                .build();
    }
}
