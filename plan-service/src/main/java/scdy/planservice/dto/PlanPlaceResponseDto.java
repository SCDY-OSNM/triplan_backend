package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.PlanPlace;
import scdy.planservice.enums.Place;

@Getter
@NoArgsConstructor
public class PlanPlaceResponseDto {
    private Long planPlaceId;

    private Long planId;

    private Place place;

    @Builder
    public PlanPlaceResponseDto(Long planPlaceId, Long planId, Place place) {
        this.planPlaceId = planPlaceId;
        this.planId = planId;
        this.place = place;
    }

    public static PlanPlaceResponseDto from(PlanPlace planPlace){
        return PlanPlaceResponseDto.builder()
                .planId(planPlace.getPlanId())
                .place(planPlace.getPlace())
                .build();
    }
}
