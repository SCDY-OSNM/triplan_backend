package scdy.planservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.Place;

@NoArgsConstructor
@Getter
public class PlanPlaceRequestDto {
    private Plan plan;

    private Place place;

    @Builder
    public PlanPlaceRequestDto(Plan plan, Place place) {
        this.plan = plan;
        this.place = place;
    }

}
