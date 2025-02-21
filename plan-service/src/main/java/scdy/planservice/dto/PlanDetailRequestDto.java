package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.enums.PlanDetailCategory;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PlanDetailRequestDto {

    private Long contentId;

    private PlanDetailCategory planCategory;

    private Long planId;

    private String planDetailName;

    private String planDetailMemo;

    private Long planDetailCost;

    private LocalDateTime planDetailTime;

    private Integer planDetailTimeLine;

    private Integer planDetailDay;

    @Builder
    public PlanDetailRequestDto(Long contentId, Long planId, PlanDetailCategory planCategory, String planDetailName, String planDetailMemo, Long planDetailCost,
                                LocalDateTime planDetailTime, Integer planDetailTimeLine, Integer planDetailDay) {
        this.planId = planId;
        this.contentId = contentId;
        this.planCategory = planCategory;
        this.planDetailName = planDetailName;
        this.planDetailMemo = planDetailMemo;
        this.planDetailCost = planDetailCost;
        this.planDetailTime = planDetailTime;
        this.planDetailTimeLine = planDetailTimeLine;
        this.planDetailDay = planDetailDay;
    }
}
