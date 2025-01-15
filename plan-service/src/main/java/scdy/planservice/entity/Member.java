package scdy.planservice.entity;

import jakarta.persistence.*;
import jakarta.persistence.Index;
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

    @Column(nullable = false)
    private Long planId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private MemberRole memberRole;


    @Builder
    public Member(Long planId, Long userId, MemberRole memberRole){
        this.planId = planId;
        this.userId = userId;
        this.memberRole = memberRole;
    }

}
