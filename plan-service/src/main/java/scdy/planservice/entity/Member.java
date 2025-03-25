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

    @ManyToOne
    @JoinColumn(name = "planId", referencedColumnName = "planId")
    private Plan plan;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private MemberRole memberRole;

    @Builder
    public Member(Plan plan, Long userId, MemberRole memberRole){
        this.plan = plan;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    public void updateMember(MemberRole memberRole){
        this.memberRole = memberRole;
    }

}
