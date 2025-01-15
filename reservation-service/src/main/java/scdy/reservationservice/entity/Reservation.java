package scdy.reservationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long contentsId;

    @Column(nullable = false)
    private Long planDetailId;

    @Column(nullable = false)
    private LocalDateTime reservationStartAt;

    @Column(nullable = false)
    private LocalDateTime reservationEndAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;


    @Builder
    public Reservation(Long userId, Long contentsId, Long planDetailId, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt, ReservationStatus reservationStatus) {
        this.userId = userId;
        this.contentsId = contentsId;
        this.planDetailId = planDetailId;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }


    public void updateReservation(
            LocalDateTime reservationStartAt,
            LocalDateTime reservationEndAt,
            ReservationStatus reservationStatus) {
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }
}
