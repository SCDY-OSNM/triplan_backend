package scdy.reservationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reservationservice.entity.enums.ReservationStatus;

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
    public Reservation(Long userId, Long contentsId, Long planDetailId, LocalDateTime reservationStartAt,
                       LocalDateTime reservationEndAt, ReservationStatus reservationStatus) {
        this.userId = userId;
        this.contentsId = contentsId;
        this.planDetailId = planDetailId;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }


    public void updateReservation(Long planDetailId, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt,
                                  ReservationStatus reservationStatus) {
        this.planDetailId = planDetailId;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }

    public void updateReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void makeReservation(Long userId){
        this.userId = userId;
        this.reservationStatus = ReservationStatus.WAITING;
    }

    public void cancelReservation(){
        this.userId = null;
        this.reservationStatus = ReservationStatus.NOT_RESERVED;
    }


}
