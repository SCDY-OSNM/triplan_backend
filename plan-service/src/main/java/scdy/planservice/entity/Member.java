package scdy.planservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import scdy.planservice.enums.MemberRole;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private Long planId;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "member_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public Member(Long planId, Long userId, MemberRole memberRole){
        this.planId = planId;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    public void updateMember(MemberRole memberRole){
        this.memberRole = memberRole;
    }

}
