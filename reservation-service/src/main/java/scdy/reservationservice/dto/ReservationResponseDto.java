package scdy.reservationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.entity.enums.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;

    private Long userId;

    private Long contentsId;

    private Long planDetailId;

    private LocalDateTime reservationStartAt;

    private LocalDateTime reservationEndAt;

    private ReservationStatus reservationStatus;

    @Builder
    public ReservationResponseDto(Long userId, Long contentsId, Long planDetailId, LocalDateTime reservationStartAt,
                                  LocalDateTime reservationEndAt, ReservationStatus reservationStatus) {
        this.userId = userId;
        this.contentsId = contentsId;
        this.planDetailId = planDetailId;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return ReservationResponseDto.builder()
                .userId(reservation.getUserId())
                .contentsId(reservation.getContentsId())
                .planDetailId(reservation.getPlanDetailId())
                .reservationStartAt(reservation.getReservationStartAt())
                .reservationEndAt(reservation.getReservationEndAt())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
