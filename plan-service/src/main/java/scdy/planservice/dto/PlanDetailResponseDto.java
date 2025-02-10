package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.enums.PlanDetailCategory;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PlanDetailResponseDto {
    private Long contentId;

    private Plan plan;

    private PlanDetailCategory planCategory;

    private String planDetailName;

    private String planDetailMemo;

    private Long planDetailCost;

    private LocalDateTime planDetailTime;

    private Integer planDetailTimeLine;

    private Integer planDetailDay;

    @Builder

    public PlanDetailResponseDto(Long contentId, Plan plan, PlanDetailCategory planCategory, String planDetailName, String planDetailMemo, Long planDetailCost, LocalDateTime planDetailTime, Integer planDetailTimeLine, Integer planDetailDay) {
        this.contentId = contentId;
        this.plan = plan;
        this.planCategory = planCategory;
        this.planDetailName = planDetailName;
        this.planDetailMemo = planDetailMemo;
        this.planDetailCost = planDetailCost;
        this.planDetailTime = planDetailTime;
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }

    public static PlanDetailResponseDto from(PlanDetail planDetail){
        return PlanDetailResponseDto.builder()
                .contentId(planDetail.getContentId())
                .plan(planDetail.getPlan())
                .planCategory(planDetail.getPlanDetailCategory())
                .planDetailName(planDetail.getPlanDetailName())
                .planDetailMemo(planDetail.getPlanDetailMemo())
                .planDetailCost(planDetail.getPlanDetailCost())
                .planDetailTime(planDetail.getPlanDetailTime())
                .planDetailTimeLine(planDetail.getPlanDetailTimeLine())
                .planDetailDay(planDetail.getPlanDetailDay())
                .build();
    }
}
