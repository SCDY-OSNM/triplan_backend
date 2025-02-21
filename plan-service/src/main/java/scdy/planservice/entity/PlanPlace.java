package scdy.planservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.planservice.enums.Place;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "planPlace")
public class PlanPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planPlaceId;

    @Column(nullable = false)
    private Long planId;

    @Column(nullable = false)
    private Place place;

    @Builder
    public PlanPlace(Long planPlaceId, Long planId, Place place) {
        this.planPlaceId = planPlaceId;
        this.planId = planId;
        this.place = place;
    }
}
