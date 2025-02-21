package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.Place;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class PlanResponseDto {
    private String planTitle;

    private LocalDate planStartAt;

    private LocalDate planEndAt;

    private Boolean isPublic;

    private Long userId;

    private List<Place> planPlace;

    @Builder

    public PlanResponseDto(String planTitle, LocalDate planStartAt, LocalDate planEndAt, Boolean isPublic, Long userId, List<Place> planPlace) {
        this.planTitle = planTitle;
        this.planStartAt = planStartAt;
        this.planEndAt = planEndAt;
        this.isPublic = isPublic;
        this.userId = userId;
        this.planPlace = planPlace;
    }

    public static PlanResponseDto from(Plan plan){
        return PlanResponseDto.builder()
                .planTitle(plan.getPlanTitle())
                .planStartAt(plan.getPlanStartAt())
                .planEndAt(plan.getPlanEndAt())
                .isPublic(plan.getIsPublic())
                .userId(plan.getUserId())
                .planPlace(plan.getPlanPlace())
                .build();
    }
}
