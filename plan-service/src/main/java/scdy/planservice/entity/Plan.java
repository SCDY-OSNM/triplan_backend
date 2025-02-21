package scdy.planservice.entity;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import scdy.planservice.enums.Place;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false)
    private String planTitle;

    @Column(nullable = false)
    private LocalDate planStartAt;

    private LocalDate planEndAt;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Long userId;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Place> planPlace;


    @Builder
    public Plan(String planTitle, LocalDate planStartAt, LocalDate planEndAt, Boolean isPublic, Long userId, List<Place> planPlace ) {
        this.planTitle = planTitle;
        this.planStartAt = planStartAt;
        this.planEndAt = planEndAt;
        this.isPublic = isPublic;
        this.planPlace = planPlace;
    }

    public void updatePlan(String planTitle, LocalDate planStartAt, LocalDate planEndAt, List<Place> planPlace){
        this.planTitle = planTitle;
        this.planStartAt = planStartAt;
        this.planEndAt = planEndAt;
        this.planPlace = planPlace;
    }

    public void changePublic(Boolean isPublic){
        this.isPublic = isPublic;
    }
}
