package scdy.planservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.enums.PlanDetailCategory;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "planDetail")
public class PlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planDetailId;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "planId", referencedColumnName = "planId")
    private Plan plan;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private PlanDetailCategory planDetailCategory;

    @Column(nullable = false)
    private String planDetailName;

    @Column(nullable = false)
    private String planDetailMemo;

    @Column(nullable = false)
    private Long planDetailCost;

    @Column(nullable = false)
    private LocalDateTime planDetailTime;

    @Column(nullable = false)
    private Integer planDetailTimeLine;

    @Column(nullable = false)
    private Integer planDetailDay;

    @Builder
    public PlanDetail(Long contentId, Plan plan, PlanDetailCategory planDetailCategory,
                      String planDetailName, String planDetailMemo, Long planDetailCost,
                      LocalDateTime planDetailTime, Integer planDetailTimeLine, Integer planDetailDay){
        this.contentId = contentId;
        this.plan = plan;
        this.planDetailCategory = planDetailCategory;
        this.planDetailName = planDetailName;
        this.planDetailMemo = planDetailMemo;
        this.planDetailCost = planDetailCost;
        this.planDetailTime = planDetailTime;
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }

    // 이름, 메모, 시간 수정
    public void updatePlanDetail(String planDetailName, String planDetailMemo, LocalDateTime planDetailTime){
        this.planDetailName = planDetailName;
        this.planDetailMemo = planDetailMemo;
        this.planDetailTime = planDetailTime;
    }

    // 순서, 날짜 수정
    public void updatePlanDetailTimeLine(Integer planDetailTimeLine, Integer planDetailDay){
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }

}
