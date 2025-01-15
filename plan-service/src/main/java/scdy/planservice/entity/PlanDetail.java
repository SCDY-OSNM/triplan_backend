package scdy.planservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.enums.PlanCategory;
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
    private Long contentId;

    @Column(nullable = false)
    private PlanCategory category;

    @Column(nullable = false)
    private String planDetailName;

    @Column(nullable = false)
    private String planDetailMemo;

    @Column(nullable = false)
    private String planDetailCost;

    @Column(nullable = false)
    private LocalDateTime planDetailTime;

    @Column(nullable = false)
    private Integer planDetailTimeLine;

    @Column(nullable = false)
    private Integer planDetailDay;

    @Builder
    public PlanDetail(Long contentId, PlanCategory category,
                      String planDetailName, String planDetailMemo, String planDetailCost,
                      LocalDateTime planDetailTime, Integer planDetailTimeLine, Integer planDetailDay){
        this.contentId = contentId;
        this.category = category;
        this.planDetailName = planDetailName;
        this.planDetailMemo = planDetailMemo;
        this.planDetailCost = planDetailCost;
        this.planDetailTime = planDetailTime;
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }

    public void updatePlanDetail(String planDetailName, String planDetailMemo){
        this.planDetailName = planDetailName;
        this.planDetailMemo =planDetailMemo;
    }

    public void updatePlanDetailTimeLine(LocalDateTime planDetailTime, Integer planDetailTimeLine, Integer planDetailDay){
        this.planDetailTime = planDetailTime;
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }


}
